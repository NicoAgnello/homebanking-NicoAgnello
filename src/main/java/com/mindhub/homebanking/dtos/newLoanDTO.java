package com.mindhub.homebanking.dtos;

import java.util.ArrayList;

public class newLoanDTO {
    private String loanName;

    private Double maxAmount;

    private ArrayList<Byte> payments;

    private Float interestRate;

    public newLoanDTO (String loanName, Double maxAmount, ArrayList<Byte> payments, Float interestRate){
        this.loanName = loanName.substring(0, 1).toUpperCase() + loanName.substring(1).toLowerCase();
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.interestRate = interestRate;
    }

    public String getLoanName() {
        return loanName;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public ArrayList<Byte> getPayments() {
        return payments;
    }

    public Float getInterestRate() {
        return interestRate;
    }
}
