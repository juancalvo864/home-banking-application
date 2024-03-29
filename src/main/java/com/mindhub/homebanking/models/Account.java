package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String number;
    private LocalDateTime creationDate;
    private Double balance;

    private CardAndAccountStatus status;

    private AccountType typeAccount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();



    public Account() {
    }

    public Account(String number, LocalDateTime creationDate, Double balance, CardAndAccountStatus status, AccountType typeAccount) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.status = status;
        this.typeAccount = typeAccount;
    }

    public void addTransaction(Transaction transaction) {
        transaction.setAccount(this);
        transactions.add(transaction);
    }


    public AccountType getTypeAccount() {
        return typeAccount;
    }


    public CardAndAccountStatus getStatus() {
        return status;
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


    public Client getClient() {
        return client;
    }
    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public long getId() {
        return id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setStatus(CardAndAccountStatus status) {
        this.status = status;
    }

    public void setTypeAccount(AccountType typeAccount) {
        this.typeAccount = typeAccount;
    }


    @Override
    public String toString() {
        return "Client{" + "Number=" + number + '\'' + ", Creation Date='" + creationDate + '\'' + ", Balance='" + balance + '\'' + '}';
    }
}
