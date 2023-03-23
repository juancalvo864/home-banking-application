package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.CardAndAccountStatus;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController{

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @RequestMapping("/clients")
    public List<ClientDTO> getAll() {
        return clientService.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClientDTOId(@PathVariable Long id) {
        return new ClientDTO(clientService.findById(id));
    }

    @RequestMapping("/clients/current")
    public ClientDTO getClientDTO(Authentication authentication) {
        return new ClientDTO( clientService.findByEmail( authentication.getName()));
    }


    @PostMapping( "/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty()) {
            return new ResponseEntity<>("Missing First Name", HttpStatus.FORBIDDEN);
        }
        if(lastName.isEmpty()){
            return new ResponseEntity<>("Missing Last Name", HttpStatus.FORBIDDEN);
        }
        if(email.isEmpty()){
            return new ResponseEntity<>("Missing Email", HttpStatus.FORBIDDEN);
        }
        if(password.isEmpty()){
            return new ResponseEntity<>("Missing Password", HttpStatus.FORBIDDEN);
        }
        if (clientService.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account newAccount = new Account(getAccountNumber(),  LocalDateTime.now(), 0.0, CardAndAccountStatus.ENABLED, AccountType.SAVING);
        newClient.addAccount(newAccount);
        clientService.save(newClient);
        accountService.save(newAccount);



        return new ResponseEntity<>(HttpStatus.CREATED);

    }



    private String getAccountNumber() {
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

