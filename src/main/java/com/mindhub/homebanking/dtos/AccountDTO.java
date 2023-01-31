package com.mindhub.homebanking.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDateTime;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;


    public AccountDTO (Account account){
    this.id = account.getId();
    this.number = account.getNumber();
    this.creationDate = account.getCreationDate();
    this.balance = account.getBalance();
    }

    public String getNumber (){
        return number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public long getId() {
        return id;
    }


    public String toString (){
        return number + " " + creationDate + " "+ balance;
    }

}
