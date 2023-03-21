package com.mindhub.homebanking.Services.Impl;


import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplementation implements AccountService {

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }
    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public Set<Account> findAll() {
        return accountRepository.findAll().stream().collect(Collectors.toSet());
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }
}
