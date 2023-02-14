package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private int cvv;

    private String cardHolder;

    private String number;

    private CardType cardType;

    private CardColor cardColor;

    private LocalDate fromDate;

    private LocalDate thruDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    public Card (){}

    public Card (int cvv, CardType cardType, CardColor cardColor, LocalDate fromDate, LocalDate thruDate, Client client ){
        this.cvv = cvv;
        this.number = (int)(1000 + Math.random() * 9000) + "-" +(int)(1000 + Math.random() * 9000)+ "-" + (int)(1000 + Math.random() * 9000)+ "-" + (int)(1000 + Math.random() * 9000) ;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.cardHolder = client.getFirstName() + " " + client.getLastName();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public long getId() {
        return id;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public void setCardColor(CardColor cardColor) {
        this.cardColor = cardColor;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }
}
