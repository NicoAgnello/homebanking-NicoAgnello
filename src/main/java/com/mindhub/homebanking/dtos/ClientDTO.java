package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private long id;
    private String firstName, lastName, email;
    private Set<AccountDTO> accounts;

    private Set<ClientLoanDTO> loans;

    private Set<CardDTO> cards;

    public ClientDTO (Client client){
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().filter(account -> account.getActive() == true).map(account -> new AccountDTO(account)).collect(Collectors.toSet());
        this.loans = client.getClientLoan().stream().map(loan -> new ClientLoanDTO(loan)).collect(Collectors.toSet());
        this.cards = client.getCards().stream().filter(card -> card.getActive() == true).map(card -> new CardDTO(card)).collect(Collectors.toSet());
    }

    public Set<CardDTO> getCards() {
        return cards;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public Long getId (){
        return id;
    }

    public String getFirstName (){
        return firstName;
    }

    public String getLastName (){
        return lastName;

    }

    public String getEmail (){
        return email;
    }


    public String toString (){
        return firstName + " " + lastName;
    }
}
