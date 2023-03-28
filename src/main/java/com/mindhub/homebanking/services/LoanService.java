package com.mindhub.homebanking.services;


import com.mindhub.homebanking.models.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {

    Boolean existsLoanById (long id);

    Boolean existsLoanByName(String name);

    List<Loan> findAll();


    void save(Loan loan);

    void saveAll(List<Loan> loans);

    Optional<Loan> findById(Long id);


}
