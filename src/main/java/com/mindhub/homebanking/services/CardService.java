package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;

import java.util.Optional;

public interface CardService {
    Optional<Card> findById(Long id);

    void save(Card card);

    Boolean existsCardByNumber(String number);

    Card findByNumber(String number);

}
