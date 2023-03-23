//package com.mindhub.homebanking;
//
//import com.mindhub.homebanking.models.ClientLoan;
//import com.mindhub.homebanking.models.Loan;
//import com.mindhub.homebanking.repositories.ClientLoanRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.hamcrest.CoreMatchers.hasItem;
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.empty;
//import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
//import static org.hamcrest.core.IsNot.not;
//
//@SpringBootTest
//public class RepositoryTestClientLoan {
//
//    @Autowired
//    ClientLoanRepository clientLoanRepository;
//
//
//
//    @Test
//    public void existClientLoans(){
//        List<ClientLoan> clientLoans = clientLoanRepository.findAll();
//        assertThat(clientLoans,is(not(empty())));
//    }
//    @Test
//    public void ClientLoanExist(){
//        List<ClientLoan> clientLoan= clientLoanRepository.findAll();
//        assertThat(clientLoan, hasItem(hasProperty("porcentage", is((byte)20))));
//    }
//
//
//
//}
