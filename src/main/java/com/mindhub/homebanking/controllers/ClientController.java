package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.configurations.WebAuthentication;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.ServicesImplementation.AccountServiceImpl;
import com.mindhub.homebanking.services.ServicesImplementation.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.Utilities.randomNumberAccount;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientService clientService;

    @GetMapping("/clients")
    public List<ClientDTO> getClients (){
       return clientService.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @GetMapping("clients/{id}")
    public ClientDTO getClient (@PathVariable Long id){
        return clientService.findById(id).map(client -> new ClientDTO(client)).orElse(null);
    }

    @PostMapping(path = "/clients")
    public ResponseEntity<Object> register (
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password){

        if (firstName.isEmpty() ) {
            return new ResponseEntity<>("Missing first name", HttpStatus.BAD_REQUEST);
        }

        if(lastName.isEmpty()){
            return new ResponseEntity<>("Missing last name", HttpStatus.BAD_REQUEST);
        }

        if(email.isEmpty()){
            return new ResponseEntity<>("Missing email", HttpStatus.BAD_REQUEST);
        }

        if(password.isEmpty()){
            return new ResponseEntity<>("Missing password", HttpStatus.BAD_REQUEST);
        }

        if (clientService.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.CONFLICT);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account account = new Account(randomNumberAccount(accountService), LocalDateTime.now(), 0, AccountType.CHECKING);
        client.addAccount(account);
        clientService.save(client);
        accountService.save(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/clients/current")
        public ClientDTO getCurrent(Authentication authentication) {
            Client client = clientService.findByEmail(authentication.getName());
            client.setAccounts(client.getAccounts().stream().filter(account -> account.getActive()).collect(Collectors.toSet()));
            client.setCards(client.getCards().stream().filter(card -> card.getActive()).collect(Collectors.toSet()));
            return new ClientDTO(client);
        }
    }

