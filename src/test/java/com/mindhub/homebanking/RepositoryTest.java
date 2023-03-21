package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.IsNot.not;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

@SpringBootTest
public class RepositoryTest {
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    TransactionRepository transactionRepository;



    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    @Test
    public void existAccount(){
        List<Account> accounts= accountRepository.findAll();
        assertThat(accounts,is(not(empty())));
    }

    @Test
    public void existClient(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("firstName", is("Melba"))));
    }

    @Test
    public void existCard(){
        List<Card> cards= cardRepository.findAll();
        assertThat(cards,is(not(empty())));
    }

    @Test
    public void ClientLoanExist(){
        List<ClientLoan> clientLoan= clientLoanRepository.findAll();
        assertThat(clientLoan, hasItem(hasProperty("porcentage", is(20))));
    }



    @Test
    public void existTransaccion(){
        List<Transaction> transactions= transactionRepository.findAll();
        assertThat(transactions,is(not(empty())));
    }
}
