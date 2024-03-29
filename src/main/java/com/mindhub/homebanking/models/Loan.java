package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String name;
    private int maxAmount;

    private byte porcentage;
@ElementCollection
@Column(name="payments")
    private List<Integer> payments = new ArrayList<>();

    @OneToMany(mappedBy="loan", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    public Loan(){};
    public Loan(String name, int maxAmount, List<Integer> payments,byte porcentage) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.porcentage = porcentage;

    }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }


    public byte getPorcentage() {
        return porcentage;
    }

    public void setPorcentage(byte porcentage) {
        this.porcentage = porcentage;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }
    @JsonIgnore
    public List<Client> getClient() {
        return clientLoans.stream().map(loan -> loan.getClient()).collect(toList());
    }

}
