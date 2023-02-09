package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository){
		return args -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Nicolas", "Agnello", "nicoagnello@mindhub.com");
			Account account1 = new Account("VIN001", LocalDateTime.now(), 5000);
			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500 );
			Account account3 = new Account("VIN003", LocalDateTime.now().plusMonths(1), 10000);
			Transaction transaction1 = new Transaction(LocalDateTime.now(), -2000.12, TransactionType.DEBIT, "First transaction");
			Transaction transaction2 = new Transaction(LocalDateTime.now(), 3000, TransactionType.CREDIT, "Second transaction");
			Transaction transaction3 = new Transaction(LocalDateTime.now(), 1000.5, TransactionType.CREDIT, "Third transaction");
			Transaction transaction4 = new Transaction(LocalDateTime.now(), -500, TransactionType.DEBIT, "Fourth transaction");
			Loan loan1 = new Loan("Hipotecario", 500000, Arrays.asList(12, 24, 36,48,60));
			Loan loan2 = new Loan("Personal", 100000, Arrays.asList(6, 12, 24));
			Loan loan3 = new Loan("Automotriz", 300000, Arrays.asList(6, 12, 24, 36));
			ClientLoan clientLoan1 = new ClientLoan(client1, loan1, 400000, (byte)24);
			ClientLoan clientLoan2 = new ClientLoan(client2, loan3, 200000, (byte)6);
			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account2.addTransaction(transaction3);
			account3.addTransaction(transaction4);
			clientRepository.save(client1);
			clientRepository.save(client2);
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
		};
	}

}
