package com.mindhub.homebanking.dtos;

public class PayPostnetDTO {
    private String cardNumber;

    private Integer cvv;

    private Double amount;

    private String description;

    public PayPostnetDTO (String cardNumber,Integer cvv, Double amount, String description){
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Integer getCvv() {
        return cvv;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
