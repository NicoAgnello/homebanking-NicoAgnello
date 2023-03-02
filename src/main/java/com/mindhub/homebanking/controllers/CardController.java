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

@RequestMapping("/client/current/cards")
public List<CardDTO> getCurrentCards (Authentication authentication){
    return clientRepository.findByEmail(authentication.getName()).getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toList());
}

@RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
public ResponseEntity<Object> newCard (@RequestParam CardColor cardColor, @RequestParam CardType cardType, Authentication authentication){

    Client client = clientRepository.findByEmail(authentication.getName());

    if (client.getCards().stream().anyMatch(card -> card.getCardColor() == cardColor && card.getCardType()==cardType)){

        if (client.getCards().stream().filter(card -> card.getCardType() == cardType).count() >= 3){
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
