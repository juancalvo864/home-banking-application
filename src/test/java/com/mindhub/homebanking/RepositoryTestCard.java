//package com.mindhub.homebanking;
//
//import com.mindhub.homebanking.models.Card;
//import com.mindhub.homebanking.models.CardColor;
//import com.mindhub.homebanking.models.CardType;
//import com.mindhub.homebanking.models.Loan;
//import com.mindhub.homebanking.repositories.CardRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static com.mindhub.homebanking.utils.CardsUtils.getCvvNumber;
//import static org.hamcrest.CoreMatchers.hasItem;
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
//import static org.hamcrest.core.IsNot.not;
//
//@SpringBootTest
//public class RepositoryTestCard {
//
//
//    @Autowired
//    CardRepository cardRepository;
//    @Test
//    public void existCard(){
//        List<Card> cards= cardRepository.findAll();
//        assertThat(cards,is(not(empty())));
//    }
//
//    @Test
//    public void existCardPropiety(){
//        List<Card> cards = cardRepository.findAll();
//        assertThat(cards, hasItem(hasProperty("color", is(CardColor.TITANIUM))));
//    }
//
//    @Test
//            public void methodCvv(){
//    int cvv = getCvvNumber();
//    assertThat(cvv, is(lessThan(999)));
//    assertThat(cvv, is(greaterThan(100)));
//    }
//
//}
