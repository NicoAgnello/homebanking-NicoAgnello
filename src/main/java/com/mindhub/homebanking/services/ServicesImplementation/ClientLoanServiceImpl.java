package com.mindhub.homebanking.services.ServicesImplementation;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientLoanServiceImpl implements ClientLoanService {
    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Override
    public void save(ClientLoan clientLoan) {

    }

    @Override
    public Optional<ClientLoan> findById(Long id) {
        return Optional.empty();
    }
}
