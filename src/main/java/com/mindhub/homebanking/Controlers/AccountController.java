package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
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
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.Controlers.ClientController.getAccountNumber;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository repo;
    @Autowired
    private ClientRepository repoClient;


    @RequestMapping("/accounts")
    public Set<AccountDTO> getAll() {
        return repo.findAll().stream().map(AccountDTO::new).collect(toSet());
    }
    @RequestMapping("/accounts/{id}")

    public AccountDTO getAccount(@PathVariable Long id){

        return  new AccountDTO(repo.findById( id).orElse(null)) ;

    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)

    public ResponseEntity<Object> createAccount(Authentication authentication) {
         Client client =  repoClient.findByEmail( authentication.getName());
         Set <Account> accounts = client.getAccounts();
         if(accounts.size() < 3 ) {
             Account newAccount = new Account(getAccountNumber(), LocalDateTime.now(), 0.0);
             client.addAccount(newAccount);
             repo.save(newAccount);

             return new ResponseEntity<>(HttpStatus.CREATED);
         }
        return new ResponseEntity<>("You can't have more than 3 accounts", HttpStatus.FORBIDDEN);
    }


}


