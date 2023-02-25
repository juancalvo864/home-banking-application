package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;


public class AccountDTO {

    private long id;

    private String number;
    private LocalDateTime creationDate;
    private Double balance;

    private Set<TransactionDTO> transactions;


    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.id = account.getId();
        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction) ).collect(Collectors.toSet());
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



}

