package com.mindhub.homebanking.controllers;

import com.lowagie.text.DocumentException;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.ServicesImplementation.AccountServiceImpl;
import com.mindhub.homebanking.services.ServicesImplementation.ClientServiceImpl;
import com.mindhub.homebanking.services.ServicesImplementation.TransactionServiceImpl;
import com.mindhub.homebanking.services.TransactionService;
import com.mindhub.homebanking.utils.TransactionsPDFExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionsController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Transactional
    @PostMapping(path = "/transactions")
    public ResponseEntity<Object> newTransaction(@RequestParam(required = false) Double amount,
                                                 @RequestParam String description,
                                                 @RequestParam String originAccountNumber,
                                                 @RequestParam String targetAccountNumber,
                                                 Authentication authentication) {

        Account originAccount = accountService.findByNumber(originAccountNumber);
        Account targetAccount = accountService.findByNumber(targetAccountNumber);

        Client client = clientService.findByEmail(authentication.getName());

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

            if (originAccountNumber.equals(targetAccountNumber)) {
                return new ResponseEntity<>("Cannot make a transaction to the same account", HttpStatus.FORBIDDEN);
            }

            if (!accountService.existsAccountByNumber(originAccountNumber)) {
                return new ResponseEntity<>("The source account does not exist", HttpStatus.BAD_REQUEST);
            }
            if (!client.getAccounts().contains(accountService.findByNumber(originAccountNumber))) {
                return new ResponseEntity<>("The account of origin does not belong to you", HttpStatus.BAD_REQUEST);
            }
            if (!accountService.existsAccountByNumber(targetAccountNumber)) {
                return new ResponseEntity<>("Target account does not exist", HttpStatus.BAD_REQUEST);
            }

            if (amount < 1) {
                return new ResponseEntity<>("Cannot transfer less than $1", HttpStatus.FORBIDDEN);
            }

            if (originAccount.getBalance() < amount) {
                return new ResponseEntity<>("The source account does not have sufficient funds", HttpStatus.FORBIDDEN);
            }

            if (!originAccount.getActive()) {
                return new ResponseEntity<>("Card inactive", HttpStatus.BAD_REQUEST);
            }

            if (!targetAccount.getActive()) {
                return new ResponseEntity<>("Card inactive", HttpStatus.BAD_REQUEST);
            }

            Transaction transactionDebit = new Transaction(LocalDateTime.now(), -amount, TransactionType.DEBIT, targetAccountNumber + ": " + description, originAccount.getBalance() - amount);
            Transaction transactionCredit = new Transaction(LocalDateTime.now(), amount, TransactionType.CREDIT, originAccountNumber + ": " + description, targetAccount.getBalance() + amount);

            originAccount.addTransaction(transactionDebit);
            targetAccount.addTransaction(transactionCredit);

            transactionService.saveAll(List.of(transactionDebit, transactionCredit));

            originAccount.setBalance(originAccount.getBalance() - amount);
            targetAccount.setBalance(targetAccount.getBalance() + amount);

            accountService.saveAll(List.of(originAccount, targetAccount));

            return new ResponseEntity<>("Transaction successfully completed", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Not an authorized client", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(path = "/transactions/filter")
    public Set<TransactionDTO> filterTransactions(@RequestParam Long accountId,
                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                                  Authentication authentication) {

        Client client = clientService.findByEmail(authentication.getName());
        Account account = client.getAccounts().stream().filter(account1 -> account1.getId() == accountId).findFirst().orElse(null);
        assert account != null;
        Set<Transaction> filteredTransactions = account.getTransactions();

        if (startDate == null && endDate != null) {
            filteredTransactions = filteredTransactions.stream().filter(transaction -> transaction.getDate().isBefore(endDate)).collect(Collectors.toSet());
        }
        if (startDate != null && endDate == null) {
            filteredTransactions = filteredTransactions.stream().filter(transaction -> transaction.getDate().isAfter(startDate)).collect(Collectors.toSet());
        }
        if (startDate != null && endDate != null) {
            filteredTransactions = filteredTransactions.stream().filter(transaction -> transaction.getDate().isBefore(endDate) && transaction.getDate().isAfter(startDate)).collect(Collectors.toSet());
        }

        return filteredTransactions.stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());

    }

    @GetMapping("/transactions/export/pdf")
    public void exportToPDF(HttpServletResponse response,
                            @RequestParam Long accountId,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                            Authentication authentication) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Transactions_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        Client client = clientService.findByEmail(authentication.getName());
        Account account = client.getAccounts().stream().filter(account1 -> account1.getId() == accountId).findFirst().orElse(null);
        assert account != null;
        Set<Transaction> filteredTransactions = account.getTransactions();

        if (startDate == null && endDate != null) {
            filteredTransactions = filteredTransactions.stream().filter(transaction -> transaction.getDate().isBefore(endDate)).collect(Collectors.toSet());
        }
        if (startDate != null && endDate == null) {
            filteredTransactions = filteredTransactions.stream().filter(transaction -> transaction.getDate().isAfter(startDate)).collect(Collectors.toSet());
        }
        if (startDate != null && endDate != null) {
            filteredTransactions = filteredTransactions.stream().filter(transaction -> transaction.getDate().isBefore(endDate) && transaction.getDate().isAfter(startDate)).collect(Collectors.toSet());
        }

        filteredTransactions.stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());


        TransactionsPDFExporter exporter = new TransactionsPDFExporter(new ArrayList<>(filteredTransactions));
        exporter.export(response);

    }
}
