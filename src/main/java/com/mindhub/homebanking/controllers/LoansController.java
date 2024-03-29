package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.dtos.newLoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.*;
import com.mindhub.homebanking.services.ServicesImplementation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoansController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientLoanService clientLoanService;

    @GetMapping("/loans")
        public List<LoanDTO> getLoans(){
            return loanService.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
        }

    @PostMapping ("/loans/add")
    public ResponseEntity<Object> newLoan (Authentication authentication, @RequestBody newLoanDTO newLoanDTO){

        Client client = clientService.findByEmail(authentication.getName());

        if(newLoanDTO.getLoanName().isEmpty()){
            return new ResponseEntity<>("Missing name of loan", HttpStatus.BAD_REQUEST);
        }

        if(newLoanDTO.getMaxAmount().isNaN()){
            return new ResponseEntity<>(" Missing or wrong max amount", HttpStatus.BAD_REQUEST);
        }

        if(newLoanDTO.getPayments().isEmpty()){
            return new ResponseEntity<>("Missing payments ", HttpStatus.BAD_REQUEST);
        }

        if (newLoanDTO.getInterestRate().isNaN()){
            return new ResponseEntity<>("Missing or wrong interest rate", HttpStatus.BAD_REQUEST);
        }

        if (newLoanDTO.getMaxAmount() < 5000){
            return new ResponseEntity<>("Loans for less than $5000 cannot be generated", HttpStatus.UNAUTHORIZED);
        }

        if (loanService.existsLoanByName(newLoanDTO.getLoanName())){
            return new ResponseEntity<>("Loan alredy exists", HttpStatus.BAD_REQUEST);
        }

        if (newLoanDTO.getInterestRate() > 2){
            return new ResponseEntity<>("The interest rate cannot exceed 200%", HttpStatus.BAD_REQUEST);
        }

        Loan newLoan = new Loan(newLoanDTO.getLoanName(), newLoanDTO.getMaxAmount(), newLoanDTO.getPayments(), newLoanDTO.getInterestRate());
        loanService.save(newLoan);

        return new ResponseEntity<>("New type of loan created", HttpStatus.CREATED);
    }


    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> newClientLoan (Authentication authentication,
                                           @RequestBody LoanApplicationDTO loanApplicationDTO){

        Client client = clientService.findByEmail(authentication.getName());
        Loan loan = loanService.findById(loanApplicationDTO.getId()).orElse(null);
        Account targetAccount = accountService.findByNumber(loanApplicationDTO.getTargetAccountNumber());

        if (loanApplicationDTO.getId() == null){
            return new ResponseEntity<>("Missing Id", HttpStatus.BAD_REQUEST);
        }

        if(loanApplicationDTO.getAmount() == null){
            return new ResponseEntity<>("Missing amount", HttpStatus.BAD_REQUEST);
        }

        if(loanApplicationDTO.getPayments() == null){
            return new ResponseEntity<>("Missing payments", HttpStatus.BAD_REQUEST);
        }

        if(loanApplicationDTO.getTargetAccountNumber() == null){
            return new ResponseEntity<>("Missing target account number", HttpStatus.BAD_REQUEST);
        }

        if (loanApplicationDTO.getAmount() <=0){
            return new ResponseEntity<>("Loans cannot be granted for $0", HttpStatus.BAD_REQUEST);
        }

        if (loanApplicationDTO.getPayments() <= 0){
            return new ResponseEntity<>("Payments cannot be 0 or less", HttpStatus.BAD_REQUEST);
        }

        if(loan == null){
            return new ResponseEntity<>("The loan does not exist", HttpStatus.BAD_REQUEST);
        }

        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("The requested loan exceeds the maximum loan amount.", HttpStatus.FORBIDDEN);
        }

        if(!loan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("We do not have this form of payment", HttpStatus.BAD_REQUEST);
        }

        if (!accountService.existsAccountByNumber(loanApplicationDTO.getTargetAccountNumber())){
            return new ResponseEntity<>("Target account does not exist", HttpStatus.BAD_REQUEST);
        }

        if (!client.getAccounts().contains(accountService.findByNumber(loanApplicationDTO.getTargetAccountNumber()))){
            return new ResponseEntity<>("The target account does not belong to you", HttpStatus.BAD_REQUEST);
        }

        if(!client.getClientLoan().stream().filter(clientLoan -> clientLoan.getLoan().getId() == loanApplicationDTO.getId()).collect(Collectors.toSet()).isEmpty()){
            return new ResponseEntity<>("Can't take same loan twice", HttpStatus.BAD_REQUEST);
        }

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() * loan.getInterestRate(), loanApplicationDTO.getPayments());
        client.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);
        clientLoanService.save(clientLoan);
        clientService.save(client);
        loanService.save(loan);

        Transaction transaction = new Transaction(LocalDateTime.now(), loanApplicationDTO.getAmount(), TransactionType.CREDIT, loan.getName() + " " + "loan approved", targetAccount.getBalance()+loanApplicationDTO.getAmount());
        targetAccount.addTransaction(transaction);
        transactionService.save(transaction);

        targetAccount.setBalance(targetAccount.getBalance() + loanApplicationDTO.getAmount());
        accountService.save(targetAccount);
        return  new ResponseEntity<>("Loan aproved and credited", HttpStatus.CREATED);

    }

}
