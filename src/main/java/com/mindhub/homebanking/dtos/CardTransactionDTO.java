package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.CardAndAccountStatus;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDate;

public class CardTransactionDTO {

    private String number;
    private int cvv;
   private Double transactionAmount;
   private String description;

    public CardTransactionDTO() {
    }

    public CardTransactionDTO(String number, int cvv, Double transactionAmount, String description) {
        this.number = number;
        this.cvv = cvv;
        this.transactionAmount = transactionAmount;
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
