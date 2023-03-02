package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;


import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository repoAccount;
    @Autowired
    private ClientRepository repoClient;


    @RequestMapping("/accounts")
    public Set<AccountDTO> getAll() {
        return repoAccount.findAll().stream().map(AccountDTO::new).collect(toSet());
    }
    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return  new AccountDTO(repoAccount.findById( id).orElse(null)) ;
    }

    @RequestMapping("/clients/current/accounts")
    public  Set<AccountDTO> getAccountCurrent(Authentication authentication){
        return  repoClient.findByEmail(authentication.getName()).getAccounts().stream().map(account-> new AccountDTO(account)).collect(toSet());
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication) {
         Client client =  repoClient.findByEmail( authentication.getName());
         Set <Account> accounts = client.getAccounts();
         if(accounts.size() < 3 ) {
             Account newAccount = new Account(getAccountNumber(), LocalDateTime.now(), 0.0);
             client.addAccount(newAccount);
             repoAccount.save(newAccount);

             return new ResponseEntity<>(HttpStatus.CREATED);
         }
        return new ResponseEntity<>("You can't have more than 3 accounts", HttpStatus.FORBIDDEN);
    }

    public String getAccountNumber() {
        Random rand  ;
        int number  ;
        String stringNumber ;
        Account account;
        do {
             rand = new Random();
             number = rand.nextInt(100000000);
             stringNumber = Integer.toString(number);
            account =  repoAccount.findByNumber(stringNumber);
        }while (account != null && account.getNumber().equals(stringNumber));
        return "VIN-" + stringNumber;
    }

}


