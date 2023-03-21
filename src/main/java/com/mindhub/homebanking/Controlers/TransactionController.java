package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.CardService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardTransactionDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;

import static com.mindhub.homebanking.models.CardAndAccountStatus.ENABLED;
import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired

    private TransactionService transactionService;

    @Autowired
    private CardService cardService;


    @GetMapping("/account/{id}")
    public ResponseEntity<Object> getAccountCurrent(@PathVariable Long id, @RequestParam(required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
        System.out.println(startDate +" " + endDate);

        if(startDate != null && endDate != null) {
            Set<TransactionDTO> setTransactions = accountService.findById(id).getTransactions().stream().filter(transaction -> transaction.getDate().isEqual(startDate) || transaction.getDate().isEqual(endDate) || transaction.getDate().isAfter(startDate) && transaction.getDate().isBefore(endDate)).map(transaction -> new TransactionDTO(transaction)).collect(toSet());
            return new ResponseEntity<>(setTransactions,HttpStatus.OK);
        }else if(startDate != null ){
         Set<TransactionDTO> setTransactions = accountService.findById(id).getTransactions().stream().filter(transaction -> transaction.getDate().isEqual(startDate) || transaction.getDate().isEqual(LocalDateTime.now()) || transaction.getDate().isAfter(startDate) && transaction.getDate().isBefore(LocalDateTime.now())).map(transaction -> new TransactionDTO(transaction)).collect(toSet());
        return new ResponseEntity<>(setTransactions,HttpStatus.OK);
         }else {
            Set<TransactionDTO> setTransactions = accountService.findById(id).getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(toSet());
            return new ResponseEntity<>(setTransactions,HttpStatus.OK);
        }
    }


    @Transactional
    @PostMapping( "/clients/transaction" )
    public ResponseEntity<Object> createTransaction(Authentication authentication, @RequestParam Double amount,
                                                    @RequestParam String description,@RequestParam String numberAccountOut,
                                                    @RequestParam String numberAccountIn) {
        Client client =  clientService.findByEmail( authentication.getName());


        if (amount == null || amount.isNaN()) {
            return new ResponseEntity<>("Missing amount", HttpStatus.FORBIDDEN);
        }
        if (amount < 0) {
            return new ResponseEntity<>("Missing amount", HttpStatus.FORBIDDEN);
        }
        if (numberAccountOut.equals(numberAccountIn)) {
            return new ResponseEntity<>("the accounts are the same", HttpStatus.FORBIDDEN);
        }
        if(description.isEmpty()){
            return new ResponseEntity<>("Missing description", HttpStatus.FORBIDDEN);
        }
        if(numberAccountOut.isEmpty()){
            return new ResponseEntity<>("Missing Number account out", HttpStatus.FORBIDDEN);
        }
        if(numberAccountIn.isEmpty()){
            return new ResponseEntity<>("Missing Number account in", HttpStatus.FORBIDDEN);
        }
        if (accountService.findByNumber(numberAccountOut) == null){
            return new ResponseEntity<>("the account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().stream().anyMatch(account -> account.getNumber().equals(numberAccountOut))) {
            return new ResponseEntity<>("Its not your account", HttpStatus.FORBIDDEN);
        }
        if (accountService.findByNumber(numberAccountIn) == null){
            return new ResponseEntity<>("the account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (accountService.findByNumber(numberAccountOut).getBalance() < amount ){
            return new ResponseEntity<>("Your balance is insufficient", HttpStatus.FORBIDDEN);
        }
        if (accountService.findByNumber(numberAccountOut).getStatus().equals(CardAndAccountStatus.DISABLED)){
            return new ResponseEntity<>("Your account is disabled", HttpStatus.FORBIDDEN);
        };


        Account accountOut =accountService.findByNumber(numberAccountOut);
        Double amountOut = accountOut.getBalance() - amount;
        accountOut.setBalance(amountOut);


        Account accountIn = accountService.findByNumber(numberAccountIn);
        Double amountIn = accountIn.getBalance() + amount;
        accountIn.setBalance(amountIn);

        Transaction transactionDebit = new Transaction(TransactionType.DEBIT,-amount,description,LocalDateTime.now(), accountOut.getBalance());
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT,amount,description,LocalDateTime.now(), accountIn.getBalance());

        accountOut.addTransaction(transactionDebit);
        accountIn.addTransaction(transactionCredit);

        transactionService.save(transactionDebit);
        transactionService.save(transactionCredit);


            return new ResponseEntity<>("The transaction was completed successfully",HttpStatus.CREATED);
        }

        @CrossOrigin
    @Transactional
    @PostMapping( "/clients/transaction/buy" )
    public ResponseEntity<Object> createTransaction( @RequestBody CardTransactionDTO cardTransactionDTO) {

        if( cardTransactionDTO.getTransactionAmount() == 0){
            return new ResponseEntity<>("The amount must be greater zero", HttpStatus.FORBIDDEN);
        }
        if( cardTransactionDTO.getCvv() > 999 || cardTransactionDTO.getCvv() < 99){
            return new ResponseEntity<>("The number entered is incorrect", HttpStatus.FORBIDDEN);
        }
        if(cardTransactionDTO.getDescription().isEmpty()){
            return new ResponseEntity<>("You must add a description", HttpStatus.FORBIDDEN);
        }
        if(cardTransactionDTO.getNumber().isEmpty()){
            return new ResponseEntity<>("You must enter a card number", HttpStatus.FORBIDDEN);
        }

        if (cardService.findByNumber(cardTransactionDTO.getNumber()) == null){
            return new ResponseEntity<>("The card number does not exist", HttpStatus.FORBIDDEN);
        }
        if(cardService.findByNumber(cardTransactionDTO.getNumber()).getType() != CardType.DEBIT){
            return new ResponseEntity<>("You card is not debit type", HttpStatus.FORBIDDEN);
        }
        if(cardService.findByNumber(cardTransactionDTO.getNumber()).getCvv() != cardTransactionDTO.getCvv()){
            return new ResponseEntity<>("wrong security code", HttpStatus.FORBIDDEN);
        }
        if(cardService.findByNumber(cardTransactionDTO.getNumber()).getThruDate().isBefore(LocalDate.now())){
            return new ResponseEntity<>("Your card is expired", HttpStatus.FORBIDDEN);
        }

        Account selectAccount = cardService.findByNumber(cardTransactionDTO.getNumber()).getClient().getAccounts().stream().sorted(Comparator.comparing(date -> date.getCreationDate())).iterator().next();
        if(selectAccount.getBalance() < cardTransactionDTO.getTransactionAmount()){
            return new ResponseEntity<>("The amount avaible in your account is insufficient", HttpStatus.FORBIDDEN);
        }

        Transaction newTransaction = new Transaction(TransactionType.DEBIT, - cardTransactionDTO.getTransactionAmount(), cardTransactionDTO.getNumber() + ":" + cardTransactionDTO.getDescription(), LocalDateTime.now(), selectAccount.getBalance());
        Double newBalance = selectAccount.getBalance() - cardTransactionDTO.getTransactionAmount();
        selectAccount.setBalance(newBalance);

        selectAccount.addTransaction(newTransaction);
        transactionService.save(newTransaction);

        return new ResponseEntity<>("The purchase was successful",HttpStatus.CREATED);
    }





}





