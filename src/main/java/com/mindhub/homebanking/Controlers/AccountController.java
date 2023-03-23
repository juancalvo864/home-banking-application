package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.*;
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


import static com.mindhub.homebanking.models.CardAndAccountStatus.DISABLED;
import static com.mindhub.homebanking.models.CardAndAccountStatus.ENABLED;
import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;


    @GetMapping("/accounts")
    public Set<AccountDTO> getAll() {
        return accountService.findAll().stream().map(AccountDTO::new).collect(toSet());
    }
    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return  new AccountDTO(accountService.findById(id));
    }

    @GetMapping("/clients/current/accounts")
    public  Set<AccountDTO> getAccountCurrent(Authentication authentication){
        return  clientService.findByEmail(authentication.getName()).getAccounts().stream().filter(account-> account.getStatus().equals(ENABLED) ).map(account-> new AccountDTO(account)).collect(toSet());
    }

    @PatchMapping("/clients/current/accounts")
    public ResponseEntity<Object> deleteAccount(Authentication authentication, @RequestParam Long accountId) {

        Client client =  clientService.findByEmail( authentication.getName());
        Account accountSelect = client.getAccounts().stream().filter(account -> account.getId() == accountId).findAny().orElse(null);

        if(accountSelect.getStatus() == DISABLED ) {
            return new ResponseEntity<>("Your account has alredy been canceled", HttpStatus.FORBIDDEN);
        }
        if(accountId == null){
            return new ResponseEntity<>("Missing card id", HttpStatus.FORBIDDEN);
        }
        if(!client.getAccounts().stream().anyMatch(account-> account.getId() == accountId)){
            return new ResponseEntity<>("The number is wrong", HttpStatus.FORBIDDEN);
        }
        if(accountSelect.getBalance() != 0) {
            return new ResponseEntity<>("To delete the account,the balance must be zero", HttpStatus.FORBIDDEN);
        };


        accountSelect.setStatus(DISABLED);
        accountService.save(accountSelect);
        return new ResponseEntity<>("The account was deleted",HttpStatus.ACCEPTED);
    }

    @PostMapping( "/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication,@RequestParam  AccountType accountType) {

         Client client =  clientService.findByEmail( authentication.getName());
         Set <Account> accounts = client.getAccounts().stream().filter(account-> account.getStatus().equals(ENABLED) ).collect(toSet());

        if(accountType == null){
            return new ResponseEntity<>("You have not entered an account type", HttpStatus.FORBIDDEN);
        }
         if(accounts.size() < 3 && accountType.equals(AccountType.SAVING) ) {
             Account newAccount = new Account(getAccountNumber(), LocalDateTime.now(), 0.0, CardAndAccountStatus.ENABLED, AccountType.SAVING);
             client.addAccount(newAccount);
             accountService.save(newAccount);
             return new ResponseEntity<>("The account has been created successfully",HttpStatus.CREATED);
         }
         if(accounts.size() < 3 && accountType.equals(AccountType.CHECKING) ) {
             Account newAccount = new Account(getAccountNumber(), LocalDateTime.now(), 0.0, CardAndAccountStatus.ENABLED, AccountType.CHECKING);
             client.addAccount(newAccount);
             accountService.save(newAccount);
             return new ResponseEntity<>("The account has been created successfully",HttpStatus.CREATED);
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
            account =  accountService.findByNumber(stringNumber);
        }while (account != null && account.getNumber().equals(stringNumber));
        return "VIN-" + stringNumber;
    }

}


