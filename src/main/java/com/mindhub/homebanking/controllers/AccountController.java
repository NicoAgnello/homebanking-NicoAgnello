package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Card;
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

    @RequestMapping("/clients/current/accounts")
    public List <AccountDTO> getCurrentAccounts( Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getAccounts().stream().filter(account -> account.getActive()==true).map(account -> new AccountDTO(account)).collect(toList());
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity <Object> newAccount (
            Authentication authentication, @RequestParam AccountType accountType
    ){
        Client client = clientRepository.findByEmail(authentication.getName());

        if(client.getAccounts().stream().filter(account -> account.getActive()).count() < 3) {
            Account account = new Account(randomNumberAccount(accountRepository), LocalDateTime.now(), 0, accountType);
            client.addAccount(account);
            accountRepository.save(account);
            return new ResponseEntity<>("Account successfully created",HttpStatus.CREATED);
        }

        else {
            return new ResponseEntity<>("Client has the maximum number of accounts(3)",HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/clients/current/accounts/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Object> deleteCard (Authentication authentication, @PathVariable Long id) {

        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findById(id).orElse(null);

        if (id == null) {
            return new ResponseEntity<>("Missing id", HttpStatus.BAD_REQUEST);
        }

        if (account == null) {
            return new ResponseEntity<>("The account does not exist", HttpStatus.BAD_REQUEST);
        }

        if (!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("Account not belong to you", HttpStatus.BAD_REQUEST);
        }
        if(!account.getActive()){
            return new ResponseEntity<>("The account is inactive", HttpStatus.BAD_REQUEST);
        }

        account.setActive(false);
        accountRepository.save(account);

        return new ResponseEntity<>("Account deleted", HttpStatus.ACCEPTED);
    }


}
