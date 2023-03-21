package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;


public class AccountDTO {

    private long id;

    private String number;
    private LocalDateTime creationDate;
    private Double balance;

    private Set<TransactionDTO> transactions;

    private CardAndAccountStatus status;

    private AccountType accountType;


    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.id = account.getId();
        this.status = account.getStatus();
        this.accountType=account.getTypeAccount();
        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction) ).collect(Collectors.toSet());

    }


    public AccountType getAccountType() {
        return accountType;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Double getBalance() {
        return balance;
    }


    public long getId() {
        return id;
    }

    public CardAndAccountStatus getStatus() {
        return status;
    }
}

