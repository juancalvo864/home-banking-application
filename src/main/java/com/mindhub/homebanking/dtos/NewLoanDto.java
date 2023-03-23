package com.mindhub.homebanking.dtos;

import java.util.List;

public class NewLoanDto {


    private String name;
    private int amount;
    private List<Integer> payments;
    private byte porcentage;


    public NewLoanDto() {
    }

    public NewLoanDto(String name, int amount, List<Integer> payments, byte porcentage) {
        this.name = name;
        this.amount = amount;
        this.payments = payments;
        this.porcentage = porcentage;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public byte getPorcentage() {
        return porcentage;
    }

    public void setPorcentage(byte porcentage) {
        this.porcentage = porcentage;
    }
}
