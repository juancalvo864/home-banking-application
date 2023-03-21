package com.mindhub.homebanking.Controlers;


import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.LoanService;
import com.mindhub.homebanking.dtos.*;
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
    private LoanService loanService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository repoTransaction;

    @Autowired
    private ClientLoanRepository repoClientLoan;





    @RequestMapping("/clients/current/loans")
    public Set<ClientLoanDTO> getLoanCurrent(Authentication authentication){
        return  clientService.findByEmail(authentication.getName()).getClientLoans().stream().map(clientLoan-> new ClientLoanDTO(clientLoan)).collect(toSet());
    }

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans( ){
        return  loanService.findAll() .stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList()) ;
    }


    @PostMapping( "/admin/loans/new" )
    public ResponseEntity<Object> createLoan(@RequestBody NewLoanDto newLoanDto){

        if(newLoanDto.getName().isEmpty()){
            return new ResponseEntity<>("Missing name", HttpStatus.BAD_REQUEST);
        }
        if(newLoanDto.getAmount() == 0){
            return new ResponseEntity<>("Missing number", HttpStatus.BAD_REQUEST);
        }
        if(newLoanDto.getPayments().isEmpty()){
            return new ResponseEntity<>("Missing payment", HttpStatus.BAD_REQUEST);
        }
        if(newLoanDto.getPorcentage() == 0) {
            return new ResponseEntity<>("Missing porcentage", HttpStatus.BAD_REQUEST);
        }
        if(!newLoanDto.getPayments().stream().anyMatch(number-> number >3 && number < 60)){
            return new ResponseEntity<>("Missing porcentage", HttpStatus.BAD_REQUEST);
        }

        Loan newLoan = new Loan( newLoanDto.getName(), newLoanDto.getAmount(), newLoanDto.getPayments(), newLoanDto.getPorcentage());

        loanService.save(newLoan);

        return new ResponseEntity<>("The loan has been created",HttpStatus.CREATED);
    }




    @Transactional
    @PostMapping( "/clients/current/loans" )
    public ResponseEntity<Object> createClientLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {

        Client client =  clientService.findByEmail( authentication.getName());
        Loan nameLoan = loanService.findById(loanApplicationDTO.getId());


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
        if(loanService.findById (loanApplicationDTO.getId()) == null){
            return new ResponseEntity<>("The loan doesn't exist.", HttpStatus.BAD_REQUEST);
        }
        if(loanApplicationDTO.getNumberAccountIn().isEmpty()){
            return new ResponseEntity<>("Missing Number account in", HttpStatus.BAD_REQUEST);
        }
        if (accountService.findByNumber(loanApplicationDTO.getNumberAccountIn()) == null){
            return new ResponseEntity<>("the account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().stream().anyMatch(account -> account.getNumber().equals(loanApplicationDTO.getNumberAccountIn()))) {
            return new ResponseEntity<>("Its not your account", HttpStatus.FORBIDDEN);
        }
        if( client.getLoans().stream().anyMatch(loan -> loan.equals(nameLoan)  ) ){
            return new ResponseEntity<>("You can't get two thing that are the same", HttpStatus.FORBIDDEN);
        }

        Account accountIn = accountService.findByNumber(loanApplicationDTO.getNumberAccountIn());
        Double amountIn = accountIn.getBalance() + loanApplicationDTO.getAmount();
        accountIn.setBalance(amountIn);

        ClientLoan newClientLoan= new ClientLoan(loanApplicationDTO.getAmount(), loanApplicationDTO.getPayments(),nameLoan) ;
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount()*1.2, nameLoan.getName() + " Loan",LocalDateTime.now(), accountIn.getBalance());





        client.addClientLoan(newClientLoan);
        loanService.findByName(nameLoan.getName()).addClientLoan(newClientLoan);
        accountIn.addTransaction(transactionCredit);


        repoTransaction.save(transactionCredit);
        repoClientLoan.save(newClientLoan);



        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
