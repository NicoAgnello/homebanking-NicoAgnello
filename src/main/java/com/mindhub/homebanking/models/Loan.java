package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String name;
    private double maxAmount;

    @ElementCollection
    @Column(name="payments")
    private List<Byte> payments = new ArrayList<>();

    @OneToMany(mappedBy="loan", fetch = FetchType.EAGER)
    private Set<ClientLoan> clients = new HashSet<>();

    public Loan (){}

    public Loan (String name, double maxAmount, List<Byte> payments){
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
    }

    public Set<ClientLoan> getClientLoan() {
        return clients;
    }

    public void setClientLoan(Set<ClientLoan> clients) {
        this.clients = clients;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Byte> getPayments() {
        return payments;
    }

    public void setPayments(List<Byte> payments) {
        this.payments = payments;
    }

    public void addClientLoan (ClientLoan clientLoan){
        clients.add(clientLoan);
        clientLoan.setLoan(this);
    }

    public List<Client> getClient() {
        return clients.stream().map(clientLoan -> clientLoan.getClient()).collect(toList());
    }

}
