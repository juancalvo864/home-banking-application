package com.mindhub.homebanking.Services.Impl;

import com.mindhub.homebanking.Services.LoanService;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImplementation implements LoanService {
    @Autowired
    private LoanRepository repoLoan;

    @Override
    public List<Loan> findAll() {
        return repoLoan.findAll();
    }

    @Override
    public void save(Loan newLoan) {
        repoLoan.save(newLoan);
    }

    @Override
    public Loan findById(Long id) {
        return repoLoan.findById(id).orElse(null);
    }

    @Override
    public Loan findByName(String name) {
        return repoLoan.findByName(name);
    }
}
