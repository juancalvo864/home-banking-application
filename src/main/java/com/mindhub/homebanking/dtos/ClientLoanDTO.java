package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private long idLoan;
    private long idClientLoan;
    private int amount;
    private byte payments;
    private String name;

    public ClientLoanDTO(){};
    public ClientLoanDTO(ClientLoan clientLoan) {
        this.idClientLoan = clientLoan.getId();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.idLoan = clientLoan.getLoan().getId() ;
        this.name = clientLoan.getLoan().getName();
    }

    public long getIdLoan() {
        return idLoan;
    }



    public long getIdClientLoan() {
        return idClientLoan;
    }



    public int getAmount() {
        return amount;
    }



    public byte getPayments() {
        return payments;
    }



    public String getName() {
        return name;
    }

}
