package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountRepository repoAccount;

    @Autowired
    private ClientRepository repoClient;

    @Autowired

    private TransactionRepository repoTransaction;


    @Transactional
    @PostMapping( "/clients/transaction" )
    public ResponseEntity<Object> createTransaction(Authentication authentication, @RequestParam Double amount,
                                                    @RequestParam String description,@RequestParam String numberAccountOut,
                                                    @RequestParam String numberAccountIn) {
        Client client =  repoClient.findByEmail( authentication.getName());


        if (amount == null || amount.isNaN()) {
            return new ResponseEntity<>("Missing amount", HttpStatus.BAD_REQUEST);
        }
        if (numberAccountOut.equals(numberAccountIn)) {
            return new ResponseEntity<>("the accounts are the same", HttpStatus.BAD_REQUEST);
        }
        if(description.isEmpty()){
            return new ResponseEntity<>("Missing description", HttpStatus.BAD_REQUEST);
        }
        if(numberAccountOut.isEmpty()){
            return new ResponseEntity<>("Missing Number account out", HttpStatus.BAD_REQUEST);
        }
        if(numberAccountIn.isEmpty()){
            return new ResponseEntity<>("Missing Number account in", HttpStatus.BAD_REQUEST);
        }
        if (repoAccount.findByNumber(numberAccountOut) == null){
            return new ResponseEntity<>("the account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().stream().anyMatch(account -> account.getNumber().equals(numberAccountOut))) {
            return new ResponseEntity<>("Its not your account", HttpStatus.FORBIDDEN);
        }
        if (repoAccount.findByNumber(numberAccountIn) == null){
            return new ResponseEntity<>("the account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (repoAccount.findByNumber(numberAccountOut).getBalance() < amount ){
            return new ResponseEntity<>("Your balance is insufficient", HttpStatus.FORBIDDEN);
        }


        Transaction transactionDebit = new Transaction(TransactionType.DEBIT,-amount,description,LocalDateTime.now());
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT,amount,description,LocalDateTime.now());

        Account accountOut = repoAccount.findByNumber(numberAccountOut);
        Double amountOut = accountOut.getBalance() - amount;
        accountOut.setBalance(amountOut);



        Account accountIn = repoAccount.findByNumber(numberAccountIn);
        Double amountIn = accountIn.getBalance() + amount;
        accountIn.setBalance(amountIn);

        accountOut.addTransaction(transactionDebit);
        accountIn.addTransaction(transactionCredit);

        repoTransaction.save(transactionDebit);
        repoTransaction.save(transactionCredit);


            return new ResponseEntity<>(HttpStatus.CREATED);
        }


}





