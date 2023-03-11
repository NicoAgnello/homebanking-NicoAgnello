package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDate;

public class CardDTO {
    private long id;

    private int cvv;

    private String cardHolder;

    private String number;

    private CardType cardType;

    private CardColor cardColor;

    private LocalDate fromDate;

    private LocalDate thruDate;

    private Boolean active;


    public CardDTO (Card card){
        this.id = card.getId();
        this.cvv = card.getCvv();
        this.cardHolder = card.getCardHolder();
        this.number = card.getNumber();
        this.cardType = card.getCardType();
        this.cardColor = card.getCardColor();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.active = card.getActive();
    }

    public Boolean getActive() {
        return active;
    }

    public long getId() {
        return id;
    }

    public int getCvv() {
        return cvv;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getNumber() {
        return number;
    }

    public CardType getCardType() {
        return cardType;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }
}
