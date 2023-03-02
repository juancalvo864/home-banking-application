package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;
import static java.time.LocalDate.now;
import static java.util.stream.Collectors.toSet;



@RestController
@RequestMapping("/api")
public class CardControler {

    @Autowired
    private CardRepository repoCard;
    @Autowired

    private ClientRepository repoClient;

    @RequestMapping("/clients/current/cards")
    public  Set<CardDTO> getCardsCurrent(Authentication authentication) {
        return repoClient.findByEmail(authentication.getName()).getCards().stream().map(card -> new CardDTO(card)).collect(toSet());
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType type, @RequestParam CardColor color) {
        if (type == null) {
            return new ResponseEntity<>("Missing Type", HttpStatus.FORBIDDEN);
        }
        if (color == null ) {
            return new ResponseEntity<>("Missing Color", HttpStatus.FORBIDDEN);
        }
        Client client =  repoClient.findByEmail( authentication.getName());
        Set<Card> cards=  client.getCards().stream().filter(card -> card.getType() == type).collect(toSet());
        if (cards.size() == 3){
            return new ResponseEntity<>("You can't have more than 3 cards", HttpStatus.FORBIDDEN);
        }
        if(cards.stream().anyMatch(card -> card.getColor() == color)){
            return new ResponseEntity<>("You can't have more than 3 cards", HttpStatus.FORBIDDEN);
        }

        Card newCard = new Card(client , type , color, addCardNumber(), getCvvNumber(), LocalDate.now(), LocalDate.now().plusYears(5));
        client.addCard(newCard);
        repoCard.save(newCard);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }




    public  String getCardNumber() {
        Random rand = new Random();
        int    number = rand.nextInt(10000);
        String stringNumber = String.format("%04d", number);
        return stringNumber;
    }

    public String addCardNumber(){

        String cardNumber;
        Card card;
        do {
         cardNumber = getCardNumber() + "-" + getCardNumber() + "-" + getCardNumber() + "-" + getCardNumber();
         card =  repoCard.findByNumber(cardNumber);
        }while (card != null && card.getNumber().equals(cardNumber));
        return cardNumber;
    }
    public static int getCvvNumber() {
        Random rand = new Random();
        int number = rand.nextInt(898)+100;
        return number;

    }


}
