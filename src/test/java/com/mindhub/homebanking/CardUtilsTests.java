package com.mindhub.homebanking;


import com.mindhub.homebanking.utils.Utilities;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.ContentDisposition.empty;

//@SpringBootTest
//public class CardUtilsTests {
//
//    @Test
//    public void cardNumberIsCreated(){
//
//        String cardNumber = Utilities.randomString();
//
//        assertThat(cardNumber,is(not(emptyOrNullString())));
//        assertThat(cardNumber, CoreMatchers.containsString("-"));
//    }
//
//    @Test
//    public void cvvIsCreated(){
//        Integer cvv = Utilities.returnCvvNumber();
//
//        assertThat(cvv, isA(Integer.class));
//        assertThat(cvv, is(not(empty())));
//    }
//
//}
