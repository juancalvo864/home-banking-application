package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
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
public class ClientController {

    @Autowired
    private ClientRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository repoAccounts;



    @RequestMapping("/clients")
    public List<ClientDTO> getAll() {
        return repo.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @RequestMapping("/clients/{id}")

    public ClientDTO getClientDTO(@PathVariable Long id) {

        return new ClientDTO(repo.findById(id).orElse(null));

    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty()) {

            return new ResponseEntity<>("Missing First Name", HttpStatus.BAD_REQUEST);
        }
        if(lastName.isEmpty()){
            return new ResponseEntity<>("Missing Last Name", HttpStatus.BAD_REQUEST);

        }
        if(email.isEmpty()){
            return new ResponseEntity<>("Missing Email", HttpStatus.BAD_REQUEST);
        }
        if(password.isEmpty()){
            return new ResponseEntity<>("Missing Password", HttpStatus.BAD_REQUEST);
        }


        if (repo.findByEmail(email) != null) {

            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);

        }

        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account newAccount = new Account(getAccountNumber(),  LocalDateTime.now(), 0.0);
        newClient.addAccount(newAccount);
        repo.save(newClient);
        repoAccounts.save(newAccount);



        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @RequestMapping("/clients/current")

    public ClientDTO getAll(Authentication authentication) {
        return new ClientDTO( repo.findByEmail( authentication.getName()));
    }

    public static String getAccountNumber() {
        Random rand = new Random();
        int number = rand.nextInt(100000000);
        String stringNumber = Integer.toString(number);
        return "VIN-" + stringNumber;
    }


}
