package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Account;

import java.util.Set;

public interface AccountService {

    void save(Account account);

    Account findByNumber(String number);

    Set<Account> findAll();

    Account findById(Long id);

}
