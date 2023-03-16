package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mindhub.homebanking.utils.Utilities.*;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository){
		return args -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba123"));
			Client client2 = new Client("Nicolas", "Agnello", "nicoagnello@mindhub.com",  passwordEncoder.encode("nico123"));
			Client client3 = new Client("admin", "admin", "admin@mindhub.com",  passwordEncoder.encode("admin"));

			Account account1 = new Account("VIN001", LocalDateTime.now(), 5000, AccountType.CHECKING);
			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500, AccountType.SAVING );
			Account account3 = new Account("VIN003", LocalDateTime.now().plusMonths(1), 10000, AccountType.CHECKING);

//			Transaction transaction1 = new Transaction(LocalDateTime.now(), -2000.12, TransactionType.DEBIT, "First transaction", account1.getBalance() - 2000.12);
//			Transaction transaction2 = new Transaction(LocalDateTime.now(), 3000, TransactionType.CREDIT, "Second transaction",2999.88 + 3000);
//			Transaction transaction3 = new Transaction(LocalDateTime.now(), 1000.5, TransactionType.CREDIT, "Third transaction", account2.getBalance() + 1000.5);
//			Transaction transaction4 = new Transaction(LocalDateTime.now(), -500, TransactionType.DEBIT, "Fourth transaction", account3.getBalance() - 500);

			Loan mortgage = new Loan("Mortgage", 500000, Arrays.asList((byte)12, (byte)24, (byte)36,(byte)48,(byte)60), 1.1f);
			Loan personal = new Loan("Personal", 100000, Arrays.asList((byte)6, (byte)12, (byte)24), 1.2f);
			Loan automotive = new Loan("Automotive", 300000, Arrays.asList((byte)6, (byte)12, (byte)24, (byte)36), 1.15f);

			ClientLoan clientLoan1 = new ClientLoan (400000, (byte)60);
			ClientLoan clientLoan2 = new ClientLoan(50000, (byte)12);
			ClientLoan clientLoan3 = new ClientLoan(100000, (byte)24);
			ClientLoan clientLoan4 = new ClientLoan(200000, (byte)36);

			Card card1 = new Card(134,"4984-6761-5828-2467", CardType.DEBIT, CardColor.GOLD, LocalDate.now(), LocalDate.now().plusYears(5), client1);
			Card card2 = new Card(returnCvvNumber(),randomNumberCard(cardRepository), CardType.CREDIT, CardColor.TITANIUM,LocalDate.now(), LocalDate.now().plusYears(5).plusMonths(2), client1);
			Card card3 = new Card(returnCvvNumber(),randomNumberCard(cardRepository), CardType.CREDIT, CardColor.SILVER,LocalDate.now(), LocalDate.now().plusYears(5), client2);
			Card card4 = new Card(returnCvvNumber(),randomNumberCard(cardRepository), CardType.DEBIT, CardColor.TITANIUM,LocalDate.now(), LocalDate.now(), client1);

			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);

			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);
			client1.addCard(card4);

//			account1.addTransaction(transaction1);
//			account1.addTransaction(transaction2);
//			account2.addTransaction(transaction3);
//			account3.addTransaction(transaction4);

			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);

			mortgage.addClientLoan(clientLoan1);
			personal.addClientLoan(clientLoan2);
			personal.addClientLoan(clientLoan3);
			automotive.addClientLoan(clientLoan4);

			clientRepository.saveAll(List.of(client1,client2,client3));

			accountRepository.saveAll(List.of(account1,account2,account3));

//			transactionRepository.saveAll(List.of(transaction1,transaction2,transaction3,transaction4));

			loanRepository.saveAll(List.of(mortgage,personal,automotive));

			clientLoanRepository.saveAll(List.of(clientLoan1,clientLoan2,clientLoan3,clientLoan4));

			cardRepository.saveAll(List.of(card1,card2,card3,card4));
		};
	}

}
