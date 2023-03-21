package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {

    List<Loan> findAll();

    void save(Loan newLoan);

    Loan findById(Long id);

    Loan findByName(String name);
}
