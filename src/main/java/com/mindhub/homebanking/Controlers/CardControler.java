package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.Services.CardService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Random;
import java.util.Set;

import static com.mindhub.homebanking.HomebankingApplication.addCardNumber;
import static com.mindhub.homebanking.models.CardAndAccountStatus.DISABLED;
import static com.mindhub.homebanking.models.CardAndAccountStatus.ENABLED;
import static com.mindhub.homebanking.utils.CardsUtils.getCardNumber;
import static com.mindhub.homebanking.utils.CardsUtils.getCvvNumber;
import static java.time.LocalDate.now;
import static java.util.stream.Collectors.toSet;



@RestController
@RequestMapping("/api")
public class CardControler {

    @Autowired
    private CardService cardService;
    @Autowired

    private ClientService clientService;

    @GetMapping ("/clients/current/cards")
    public  Set<CardDTO> getCardsCurrent(Authentication authentication) {
        return clientService.findByEmail(authentication.getName()).getCards().stream().filter(card-> card.getStatus().equals(ENABLED) ).map(card -> new CardDTO(card)).collect(toSet());
    }

    @PatchMapping("/clients/current/cards")
    public ResponseEntity<Object> deleteCard(Authentication authentication, @RequestParam Long cardId) {
        Client client =  clientService.findByEmail( authentication.getName());

        if(cardId == null){
            return new ResponseEntity<>("Missing card id", HttpStatus.FORBIDDEN);
        }
        if(!client.getCards().stream().anyMatch(card -> card.getId() == cardId)){
            return new ResponseEntity<>("The number is wrong", HttpStatus.FORBIDDEN);
        }

        Card cardSelect = client.getCards().stream().filter(card -> card.getId() == cardId).findAny().orElse(null);
        cardSelect.setStatus(DISABLED);
        cardService.save(cardSelect);
        return new ResponseEntity<>("The card was deleted",HttpStatus.ACCEPTED);
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType type, @RequestParam CardColor color) {
        Client client = clientService.findByEmail( authentication.getName());
        Set<Card> cards=  client.getCards().stream().filter(card -> card.getType() == type).collect(toSet());

        if (type == null) {
            return new ResponseEntity<>("Missing Type", HttpStatus.FORBIDDEN);
        }
        if (color == null ) {
            return new ResponseEntity<>("Missing Color", HttpStatus.FORBIDDEN);
        }
        if (cards.size() == 3){
            return new ResponseEntity<>("You can't have more than 3 cards", HttpStatus.FORBIDDEN);
        }
        if(cards.stream().anyMatch(card -> card.getColor() == color)){
            return new ResponseEntity<>("You can't have same card", HttpStatus.FORBIDDEN);
        }

        String cardNumber;
        Card card;
        do {
            cardNumber = getCardNumber() + "-" + getCardNumber() + "-" + getCardNumber() + "-" + getCardNumber();
            card =  cardService.findByNumber(cardNumber);
        }while (card != null && card.getNumber().equals(cardNumber));


        Card newCard = new Card(client , type , color, cardNumber, getCvvNumber(), LocalDate.now(), LocalDate.now().plusYears(5),ENABLED);
        client.addCard(newCard);
        cardService.save(newCard);

            return new ResponseEntity<>("The card has been created successfully",HttpStatus.CREATED);
        }







}
