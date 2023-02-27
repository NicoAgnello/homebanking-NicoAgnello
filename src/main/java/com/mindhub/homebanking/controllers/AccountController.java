package com.mindhub.homebanking.controllers;

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
import java.util.List;

import static com.mindhub.homebanking.utils.Utilities.randomNumberAccount;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;
    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts (){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }
    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount (@PathVariable Long id){
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity <Object> newAccount (
            Authentication authentication
    ){
        Client client = clientRepository.findByEmail(authentication.getName());
        if(client.getAccounts().size() < 3) {
            Account account = new Account(randomNumberAccount(accountRepository), LocalDateTime.now(), 0);
            client.addAccount(account);
            accountRepository.save(account);
            return new ResponseEntity<>("Account successfully created",HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>("Client has the maximum number of accounts(3)",HttpStatus.FORBIDDEN);
        }
    }


}
