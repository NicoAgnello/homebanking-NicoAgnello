package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
}
