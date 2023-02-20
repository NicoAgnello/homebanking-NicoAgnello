package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name= "native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    private String password;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientLoan> loans = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    public Client (){}

    public Client (String first, String last, String email, String password){
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.password = password;
    }
    public void addCard(Card card){
        cards.add(card);
        card.setClient(this);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Card> getCards() {
        return cards;
    }


    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public void setClientLoan(Set<ClientLoan> loans) {
        this.loans = loans;
    }

    public Set<ClientLoan> getClientLoan() {
        return loans;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Long getId (){
        return id;
    }

    public String getFirstName (){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName (){
        return lastName;

    }

    public void setLastName (String lastName){
        this.lastName = lastName;
    }

    public String getEmail (){
        return email;
    }

    public void setEmail (String email){
        this.email = email;
    }

    public String toString (){
        return firstName + " " + lastName;
    }

    public void addClientLoan (ClientLoan clientLoan){
        loans.add(clientLoan);
        clientLoan.setClient(this);
    }

    public void addAccount (Account account){
        account.setClient(this);
        accounts.add(account);
    }

    @JsonIgnore
    public List<Loan> getLoan() {
        return loans.stream().map(clientLoan -> clientLoan.getLoan()).collect(toList());
    }

}
