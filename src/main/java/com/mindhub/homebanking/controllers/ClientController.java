package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.configurations.WebAuthentication;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.utils.Utilities.randomNumberAccount;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;
    @RequestMapping("/clients")
    public List<ClientDTO> getClients (){
       return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }
    @RequestMapping("clients/{id}")
    public ClientDTO getClient (@PathVariable Long id){
        return clientRepository.findById(id).map(client -> new ClientDTO(client)).orElse(null);
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
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

        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.CONFLICT);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account account = new Account(randomNumberAccount(accountRepository), LocalDateTime.now(), 0);
        client.addAccount(account);
        clientRepository.save(client);
        accountRepository.save(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @RequestMapping(path = "/clients/current", method = RequestMethod.GET)

    public ClientDTO getCurrent(Authentication authentication) {

        return new ClientDTO( clientRepository.findByEmail( authentication.getName()));

    }
    }

