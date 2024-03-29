package com.mindhub.homebanking.dtos;


import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.models.CardAndAccountStatus.ENABLED;
import static java.util.stream.Collectors.toSet;

public class ClientDTO {

    private long id;


    private String firstName;
    private String lastName;

    private String email;
    private Set<AccountDTO> accounts;
    private Set<ClientLoanDTO> loans;

    private Set<CardDTO> cards;



    public ClientDTO(Client client) {

        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().filter(account-> account.getStatus().equals(ENABLED) ).map(account -> new AccountDTO(account) ).collect(toSet());
        this.loans = client.getClientLoans().stream().map(clientloan -> new ClientLoanDTO(clientloan)).collect(toSet());
        this.cards = client.getCards().stream().filter(card-> card.getStatus().equals(ENABLED) ).map(CardDTO::new).collect(toSet());

    }



    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public String getEmail() {
        return email;
    }


    public long getId() {
        return id;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }
}




