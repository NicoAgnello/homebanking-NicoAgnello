package com.mindhub.homebanking;


import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.ContentDisposition.empty;


// PARA H2

//@SpringBootTest

//PARA POSTGRES

//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//
//
//public class RepositoriesTest {
//
//    @Autowired
//
//    LoanRepository loanRepository;
//
//    @Autowired
//    AccountRepository accountRepository;
//
//    @Autowired
//    ClientRepository clientRepository;
//
//    @Autowired
//    CardRepository cardRepository;
//
//    @Autowired
//    TransactionRepository transactionRepository;
//
//
//    @Test
//
//    public void existLoans(){
//
//        List<Loan> loans = loanRepository.findAll();
//
//        assertThat(loans,is(not(empty())));
//
//    }
//
//    @Test
//
//    public void existPersonalLoan(){
//
//        List<Loan> loans = loanRepository.findAll();
//
//        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
//
//    }
//
//    @Test
//    public void existAccounts (){
//        List<Account> accounts = accountRepository.findAll();
//
//        assertThat(accounts, is(not(empty())));
//    }
//
//    @Test
//    public void existTypeAccount(){
//        List<Account> accounts = accountRepository.findAll();
//
//        assertThat(accounts, hasItem(hasProperty("accountType", is(AccountType.SAVING))));
//    }
//
//    @Test
//    public void existClients(){
//        List<Client> clients = clientRepository.findAll();
//
//        assertThat(clients, is(not(empty())));
//    }
//
//    @Test
//    public void existPropClients(){
//        List<Client> clients = clientRepository.findAll();
//
//        assertThat(clients, hasItem(hasProperty("email", isA(String.class))));
//    }
//
//
//    @Test
//    public  void existCards(){
//        List<Card> cards = cardRepository.findAll();
//
//        assertThat(cards, is(not(empty())));
//    }
//
//    @Test
//    public  void existPropCards(){
//        List<Card> cards = cardRepository.findAll();
//
//        assertThat(cards, hasItem(hasProperty("fromDate", isA(LocalDate.class))));
//    }
//
//    @Test
//    public void existTransaction (){
//        List<Transaction> transactions = transactionRepository.findAll();
//
//        assertThat(transactions, is(not(empty())));
//    }
//
//    @Test
//    public void existPropTransaction (){
//        List<Transaction> transactions = transactionRepository.findAll();
//
//        assertThat(transactions, hasItem(hasProperty("description", isA(String.class))));
//    }
//}
