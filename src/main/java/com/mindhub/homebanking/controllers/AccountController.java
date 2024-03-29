package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.ServicesImplementation.AccountServiceImpl;
import com.mindhub.homebanking.services.ServicesImplementation.ClientServiceImpl;
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
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts (){
        return accountService.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount (@PathVariable Long id){
        return accountService.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    @GetMapping("/clients/current/accounts")
    public List <AccountDTO> getCurrentAccounts( Authentication authentication){
        Client client = clientService.findByEmail(authentication.getName());
        return client.getAccounts().stream().filter(account -> account.getActive()==true).map(account -> new AccountDTO(account)).collect(toList());
    }

    @PostMapping(path = "/clients/current/accounts")
    public ResponseEntity <Object> newAccount (
            Authentication authentication, @RequestParam AccountType accountType
    ){
        Client client = clientService.findByEmail(authentication.getName());

        if(client.getAccounts().stream().filter(account -> account.getActive()).count() < 3) {
            Account account = new Account(randomNumberAccount(accountService), LocalDateTime.now(), 0, accountType);
            client.addAccount(account);
            accountService.save(account);
            return new ResponseEntity<>("Account successfully created",HttpStatus.CREATED);
        }

        else {
            return new ResponseEntity<>("Client has the maximum number of accounts(3)",HttpStatus.FORBIDDEN);
        }
    }

    @PatchMapping(path = "/clients/current/accounts/{id}")
    public ResponseEntity<Object> deleteAcount (Authentication authentication, @PathVariable Long id) {

        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id).orElse(null);

        if (id == null) {
            return new ResponseEntity<>("Missing id", HttpStatus.BAD_REQUEST);
        }

        if (account == null) {
            return new ResponseEntity<>("The account does not exist", HttpStatus.BAD_REQUEST);
        }

        if (!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("Account not belong to you", HttpStatus.BAD_REQUEST);
        }

        if (account.getBalance() > 0 ){
            return new ResponseEntity<>("An account cannot be deleted if it contains money", HttpStatus.BAD_REQUEST);
        }

        if(!account.getActive()){
            return new ResponseEntity<>("The account is inactive", HttpStatus.BAD_REQUEST);
        }

        if (!client.getClientLoan().isEmpty() && client.getAccounts().size()==1){
            return new ResponseEntity<>("Cannot delete an account if you have a loan and only one account", HttpStatus.BAD_REQUEST);
        }

        account.setActive(false);
        accountService.save(account);

        return new ResponseEntity<>("Account deleted", HttpStatus.ACCEPTED);
    }


}
