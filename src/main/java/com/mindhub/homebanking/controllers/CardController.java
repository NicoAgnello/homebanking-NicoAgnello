package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.PayPostnetDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.Utilities.randomNumberCard;
import static com.mindhub.homebanking.utils.Utilities.returnCvvNumber;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
        private ClientRepository clientRepository;

    @Autowired
        private CardRepository cardRepository;

    @Autowired
        private AccountRepository accountRepository;

    @Autowired
        private TransactionRepository transactionRepository;

    @GetMapping("/cards")
    public List<CardDTO> getCards (Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toList());
    }

    @RequestMapping("/clients/current/cards")
    public List<CardDTO> getCurrentCards (Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getCards().stream().filter(card -> card.getActive()==true).map(card -> new CardDTO(card)).collect(Collectors.toList());
    }

    @RequestMapping(path = "/clients/current/cards/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Object> deleteCard (Authentication authentication, @PathVariable Long id) {

        Client client = clientRepository.findByEmail(authentication.getName());
        Card card = cardRepository.findById(id).orElse(null);

        if (id == null) {
            return new ResponseEntity<>("Missing id", HttpStatus.BAD_REQUEST);
        }

        if (card == null) {
            return new ResponseEntity<>("The card does not exist", HttpStatus.BAD_REQUEST);
        }

        if (!client.getCards().contains(card)) {
            return new ResponseEntity<>("Card not belong to you", HttpStatus.BAD_REQUEST);
        }
        if(!card.getActive()){
            return new ResponseEntity<>("The card is inactive", HttpStatus.BAD_REQUEST);
        }

        card.setActive(false);
        cardRepository.save(card);

        return new ResponseEntity<>("Card deleted", HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> newCard (@RequestParam CardColor cardColor, @RequestParam CardType cardType, Authentication authentication){

        Client client = clientRepository.findByEmail(authentication.getName());

        Set<Card> activeCards = client.getCards().stream().filter(card -> card.getActive()).collect(Collectors.toSet());
        Set<Card> inactiveCards = client.getCards().stream().filter(card -> !card.getActive()).collect(Collectors.toSet());

        if (activeCards.stream().anyMatch(card -> card.getCardColor() == cardColor && card.getCardType()==cardType)){

            if (activeCards.stream().filter(card -> card.getCardType() == cardType).count() >= 3){
                return new ResponseEntity<>("You have the max of " + " " +cardType.toString().toLowerCase() + " " + "cards created", HttpStatus.CONFLICT);
            }

            else {
                return new ResponseEntity<>("You have alredy the same card", HttpStatus.CONFLICT);
            }

        }

        Card card = new Card(returnCvvNumber(),randomNumberCard(cardRepository),cardType,cardColor, LocalDate.now(), LocalDate.now().plusYears(5), client);
        client.addCard(card);
        cardRepository.save(card);

        return new ResponseEntity<>("Card successfully created",HttpStatus.CREATED);
    }

    @CrossOrigin("*")
    @PostMapping(path = "/cards/postnet")
    @Transactional
    public ResponseEntity<Object> payWithPostnet (@RequestBody PayPostnetDTO payPostnetDTO){

        String cardNumber = payPostnetDTO.getCardNumber();
        Integer cvv = payPostnetDTO.getCvv();
        Double amount = payPostnetDTO.getAmount();
        String description = payPostnetDTO.getDescription();

        Card card = cardRepository.findByNumber(cardNumber);
        Client client = card.getClient();
        Account account = client.getAccounts().stream().filter(account1 -> account1.getBalance() >= amount).findFirst().orElse(null);

        if (!cardRepository.existsCardByNumber(cardNumber)){
            return  new ResponseEntity<>("The card does not exist", HttpStatus.BAD_REQUEST);
        }

        if(cardNumber == null){
            return new ResponseEntity<>("Missing card number", HttpStatus.BAD_REQUEST);
        }

        if(amount.isNaN()){
            return  new ResponseEntity<>("Enter a correct amount", HttpStatus.BAD_REQUEST);
        }

        if(description == null){
            return  new ResponseEntity<>( "Missing description", HttpStatus.BAD_REQUEST);
        }

        if (cvv == null){
            return  new ResponseEntity<>( "Missing security code", HttpStatus.BAD_REQUEST);
        }

        if (card.getCvv() != cvv){
            return new ResponseEntity<>("The security code is incorrect", HttpStatus.BAD_REQUEST);
        }

        if (card.getCardType() == CardType.CREDIT){
            return new ResponseEntity<>("Only debit cards accepted", HttpStatus.BAD_REQUEST);
        }

        if (!card.getActive()){
            return  new ResponseEntity<>("The card is out of service", HttpStatus.BAD_REQUEST);
        }

        if (!card.getThruDate().isAfter(LocalDate.now()) ){
            return new ResponseEntity<>("The card is expired", HttpStatus.BAD_REQUEST);
        }

        if (account==null){
            return new ResponseEntity<>("The customer does not have an account associated with his profile.", HttpStatus.BAD_REQUEST);
        }

        if(!account.getActive()){
            return new ResponseEntity<>("The account is out of service", HttpStatus.BAD_REQUEST);
        }

        if (account.getBalance() < amount){
            return new ResponseEntity<>("The account does not have sufficient funds", HttpStatus.BAD_REQUEST);
        }

        Transaction transactionPostnet = new Transaction(LocalDateTime.now(), -amount, TransactionType.DEBIT, "Pay with postnet: "+ description , account.getBalance()-amount);

        transactionRepository.save(transactionPostnet);

        account.addTransaction(transactionPostnet);
        account.setBalance(account.getBalance()-amount);

        accountRepository.save(account);

        return new ResponseEntity<>("Pay with postnet successfully completed", HttpStatus.CREATED);
    }
}
