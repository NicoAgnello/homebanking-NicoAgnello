package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanDTO {
    private long id;

    private String name;

    private double maxAmount;

    private float interestRate;


    private List<Byte> payments;

    public LoanDTO(Loan loan){
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.interestRate= loan.getInterestRate();
    }

    public float getInterestRate() {
        return interestRate;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public List<Byte> getPayments() {
        return payments;
    }
}
