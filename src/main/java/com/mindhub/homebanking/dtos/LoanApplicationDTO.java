package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

public class LoanApplicationDTO {

    private long id;

    private Integer amount;

    private Byte payments;

    private String numberAccountIn;

    public LoanApplicationDTO(long id, Integer amount, Byte payments, String numberAccountIn) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.numberAccountIn = numberAccountIn;
    }

    public long getId() {
        return id;
    }

    public Integer getAmount() {
        return amount;
    }

    public Byte getPayments() {
        return payments;
    }

    public String getNumberAccountIn() {
        return numberAccountIn;
    }
}
