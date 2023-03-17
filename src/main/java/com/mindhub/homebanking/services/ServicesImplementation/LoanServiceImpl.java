package com.mindhub.homebanking.services.ServicesImplementation;

import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public Boolean existsLoanById(long id) {
        return loanRepository.existsLoanById(id);
    }

    @Override
    public Boolean existsLoanByName(String name) {
        return loanRepository.existsLoanByName(name);
    }

    @Override
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    @Override
    public void save(Loan loan) {
        loanRepository.save(loan);
    }

    @Override
    public void saveAll(List<Loan> loans) {
        loanRepository.saveAll(loans);
    }

    @Override
    public Optional<Loan> findById(Long id) {
        return loanRepository.findById(id);
    }
}
