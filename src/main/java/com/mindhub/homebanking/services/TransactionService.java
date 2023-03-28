package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    void saveAll(List<Transaction> transactions);

    void save(Transaction transaction);

    Optional<Transaction> findById(Long id);

}
