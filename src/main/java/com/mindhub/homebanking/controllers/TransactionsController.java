package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionsController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> newTransaction (@RequestParam (required = false) Double amount,
                                                  @RequestParam String description,
                                                  @RequestParam String originAccountNumber,
                                                  @RequestParam String targetAccountNumber,
                                                  Authentication authentication){

        Account originAccount = accountRepository.findByNumber(originAccountNumber);
        Account targetAccount = accountRepository.findByNumber(targetAccountNumber);

        Client client = clientRepository.findByEmail(authentication.getName());

        if (client != null) {

            if (amount == null) {
                return new ResponseEntity<>("Missing amount", HttpStatus.BAD_REQUEST);
            }

            if (description.isEmpty()) {
                return new ResponseEntity<>("Missing description", HttpStatus.BAD_REQUEST);
            }

            if (originAccountNumber.isEmpty()) {
                return new ResponseEntity<>("Missing account number of origin", HttpStatus.BAD_REQUEST);
            }

            if (targetAccountNumber.isEmpty()) {
                return new ResponseEntity<>("Missing the destination account number", HttpStatus.BAD_REQUEST);
            }

            if (originAccountNumber.equals(targetAccountNumber)){
                return new ResponseEntity<>("Cannot make a transaction to the same account", HttpStatus.FORBIDDEN);
            }

            if (!accountRepository.existsAccountByNumber(originAccountNumber)){
                return new ResponseEntity<>("The source account does not exist", HttpStatus.BAD_REQUEST);
            }
            if (!client.getAccounts().contains(accountRepository.findByNumber(originAccountNumber))){
                return new ResponseEntity<>("The account of origin does not belong to you", HttpStatus.BAD_REQUEST);
            }
            if (!accountRepository.existsAccountByNumber(targetAccountNumber)){
                return new ResponseEntity<>("Target account does not exist", HttpStatus.BAD_REQUEST);
            }

            if(amount < 1){
                return new ResponseEntity<>("Cannot transfer less than $1", HttpStatus.FORBIDDEN);
            }

            if(originAccount.getBalance() < amount){
                return new ResponseEntity<>("The source account does not have sufficient funds", HttpStatus.FORBIDDEN);
            }

            Transaction transactionDebit = new Transaction(LocalDateTime.now(),-amount, TransactionType.DEBIT, targetAccountNumber + ": " + description, originAccount.getBalance()-amount);
            Transaction transactionCredit = new Transaction(LocalDateTime.now(), amount, TransactionType.CREDIT, originAccountNumber+ ": " + description, targetAccount.getBalance()+amount);

            originAccount.addTransaction(transactionDebit);
            targetAccount.addTransaction(transactionCredit);

            transactionRepository.saveAll(List.of(transactionDebit,transactionCredit));

            originAccount.setBalance(originAccount.getBalance() - amount);
            targetAccount.setBalance(targetAccount.getBalance() + amount);

            accountRepository.saveAll(List.of(originAccount,targetAccount));

            return new ResponseEntity<>("Transaction successfully completed", HttpStatus.CREATED);
            }


        else {
            return new ResponseEntity<>("Not an authorized client",HttpStatus.UNAUTHORIZED);
        }
    }
}
