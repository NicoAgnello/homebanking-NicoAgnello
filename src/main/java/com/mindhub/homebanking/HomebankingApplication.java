package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.LocalDate;
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
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository){
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

			Loan loan1 = new Loan("Mortgage", 500000, Arrays.asList(12, 24, 36,48,60));
			Loan loan2 = new Loan("Personal", 100000, Arrays.asList(6, 12, 24));
			Loan loan3 = new Loan("Automotive", 300000, Arrays.asList(6, 12, 24, 36));

			ClientLoan clientLoan1 = new ClientLoan(client1, loan1, 400000, (byte)60);
			ClientLoan clientLoan2 = new ClientLoan(client1, loan2, 50000, (byte)12);
			ClientLoan clientLoan3 = new ClientLoan(client2, loan2, 100000, (byte)24);
			ClientLoan clientLoan4 = new ClientLoan(client2, loan2, 200000, (byte)36);

			Card card1 = new Card(returnCvvNumber(),randomNumberCard(cardRepository), CardType.DEBIT, CardColor.GOLD, LocalDate.now(), LocalDate.now().plusYears(5), client1);
			Card card2 = new Card(returnCvvNumber(),randomNumberCard(cardRepository), CardType.CREDIT, CardColor.TITANIUM,LocalDate.now(), LocalDate.now().plusYears(5), client1);
			Card card3 = new Card(returnCvvNumber(),randomNumberCard(cardRepository), CardType.CREDIT, CardColor.SILVER,LocalDate.now(), LocalDate.now().plusYears(5), client2);

			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);

			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);

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
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
		};
	}
	public static int returnCvvNumber(){
		int number;
		number= (int) (Math.floor(Math.random() * (999-100)) + 100);
		return number;
	}
	public static String randomNumberCard(CardRepository cardRepository){
		String newNumber;
		Boolean cardOptional;
		do {
			newNumber= randomString();
			cardOptional= cardRepository.existsCardByNumber(newNumber);
		}while(cardOptional);
		return newNumber;
	}
	public static String randomString(){
		int number1_1 = (int) (Math.random() * (5- 4+ 1)+4);
		int number1 = (int) (Math.random() * (999 - 100  + 1) + 100);
		int number2 = (int) (Math.random() * (9999 - 1000  + 1) + 1000);
		int number3 = (int) (Math.random() * (9999 - 1000  + 1) + 1000);
		int number4 = (int) (Math.random() * (9999 - 1000  + 1) + 1000);
		return  number1_1+""+number1+"-"+number2+"-"+number3+"-"+number4;
	}

}
