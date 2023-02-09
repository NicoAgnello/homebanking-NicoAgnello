package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name= "native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    Set<ClientLoan> loans = new HashSet<>();

    public void addLoan (ClientLoan clientLoan){
        loans.add(clientLoan);
        clientLoan.setClient(this);
    }

    public Set<ClientLoan> getLoans() {
        return loans;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void addAccount (Account account){
        account.setClient(this);
        accounts.add(account);
    }

    public Client (){}

    public Client (String first, String last, String email){
        this.firstName = first;
        this.lastName = last;
        this.email = email;
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
}
