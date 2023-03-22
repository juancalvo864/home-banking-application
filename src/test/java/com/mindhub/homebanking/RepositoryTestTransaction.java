package com.mindhub.homebanking;

import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.IsNot.not;

@SpringBootTest
public class RepositoryTestTransaction {

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void existTransaccion(){
        List<Transaction> transactions= transactionRepository.findAll();
        assertThat(transactions,is(not(empty())));
    }

    @Test
    public void transaccionExist(){
            List<Transaction> transactions= transactionRepository.findAll();
            assertThat(transactions, hasItem(hasProperty("type", is(TransactionType.DEBIT))));
        }
    }

