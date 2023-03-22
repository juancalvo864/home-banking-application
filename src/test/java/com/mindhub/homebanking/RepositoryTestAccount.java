package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.IsNot.not;
@SpringBootTest

public class RepositoryTestAccount {
    @Autowired
    AccountRepository accountRepository;
    @Test
    public void existAccount(){
        List<Account> accounts= accountRepository.findAll();
        assertThat(accounts,is(not(empty())));
    }
}
