package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public class CardsUtils {


    public CardsUtils() {
    }

    public static   String getCardNumber() {
        Random rand = new Random();
        int    number = rand.nextInt(10000);
        String stringNumber = String.format("%04d", number);
        return stringNumber;
    }


    public static int getCvvNumber() {
        Random rand = new Random();
        int number = rand.nextInt(899)+100;
        return number;

    }
}
