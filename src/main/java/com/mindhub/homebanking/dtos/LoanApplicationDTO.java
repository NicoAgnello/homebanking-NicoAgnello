package com.mindhub.homebanking.dtos;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class LoanApplicationDTO {

    private Long id;

    private Double amount;

    private Byte payments;

    private String targetAccountNumber;

    public LoanApplicationDTO (Long id, double amount, Byte payments, String targetAccountNumber){
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.targetAccountNumber=targetAccountNumber;
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public Byte getPayments() {
        return payments;
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

}
