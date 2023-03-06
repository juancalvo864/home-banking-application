package com.mindhub.homebanking.Controlers;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanRepository repoLoan;

    @Autowired
    private ClientRepository repoClient;

    @Autowired
    private AccountRepository repoAccount;

    @Autowired
    private TransactionRepository repoTransaction;

    @Autowired
    private ClientLoanRepository repoClientLoan;





    @RequestMapping("/clients/current/loans")
    public Set<ClientLoanDTO> getLoanCurrent(Authentication authentication){
        return  repoClient.findByEmail(authentication.getName()).getClientLoans().stream().map(clientLoan-> new ClientLoanDTO(clientLoan)).collect(toSet());
    }

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans( ){
        return  repoLoan.findAll() .stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList()) ;
    }

    @Transactional
    @PostMapping( "/clients/current/loans" )
    public ResponseEntity<Object> createTransaction(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {

        Client client =  repoClient.findByEmail( authentication.getName());
        Loan nameLoan = repoLoan.findById(loanApplicationDTO.getId()).orElse( null);

        if (loanApplicationDTO.getAmount() == null || loanApplicationDTO.getAmount() == 0) {
            return new ResponseEntity<>("Missing amount", HttpStatus.BAD_REQUEST);
        }
        if(loanApplicationDTO.getPayments() == null || loanApplicationDTO.getPayments() == 0){
            return new ResponseEntity<>("Missing payments", HttpStatus.BAD_REQUEST);
        }
        if(nameLoan.getPayments().stream().anyMatch(payment -> payment.equals(loanApplicationDTO.getPayments()) )){
            return new ResponseEntity<>("The amount of payment entered is incorrect", HttpStatus.BAD_REQUEST);
        }
        if(nameLoan.getMaxAmount() < loanApplicationDTO.getAmount()){
            return new ResponseEntity<>("The requested amount exceeds the maximum", HttpStatus.BAD_REQUEST);
        }
        if(repoLoan.findById (loanApplicationDTO.getId()) == null){
            return new ResponseEntity<>("The loan doesn't exist.", HttpStatus.BAD_REQUEST);
        }
        if(loanApplicationDTO.getNumberAccountIn().isEmpty()){
            return new ResponseEntity<>("Missing Number account in", HttpStatus.BAD_REQUEST);
        }
        if (repoAccount.findByNumber(loanApplicationDTO.getNumberAccountIn()) == null){
            return new ResponseEntity<>("the account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().stream().anyMatch(account -> account.getNumber().equals(loanApplicationDTO.getNumberAccountIn()))) {
            return new ResponseEntity<>("Its not your account", HttpStatus.FORBIDDEN);
        }



        ClientLoan newClientLoan= new ClientLoan(loanApplicationDTO.getAmount(), loanApplicationDTO.getPayments()) ;
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount()*1.2, nameLoan + " Loan",LocalDateTime.now());



        Account accountIn = repoAccount.findByNumber(loanApplicationDTO.getNumberAccountIn());
        Double amountIn = accountIn.getBalance() + loanApplicationDTO.getAmount();
        accountIn.setBalance(amountIn);

        client.addClientLoan(newClientLoan);
        repoLoan.findByName(nameLoan.getName()).addClientLoan(newClientLoan);
        accountIn.addTransaction(transactionCredit);


        repoTransaction.save(transactionCredit);
        repoClientLoan.save(newClientLoan);



        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
