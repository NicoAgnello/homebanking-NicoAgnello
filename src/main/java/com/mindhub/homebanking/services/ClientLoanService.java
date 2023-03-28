package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.ClientLoan;

import java.util.Optional;

public interface ClientLoanService {
    void save(ClientLoan clientLoan);

    Optional<ClientLoan> findById(Long id);

}
