package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.Utilities;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.http.ContentDisposition.empty;

//@SpringBootTest
//
//public class AccountUtilsTests {
//    @Test
//    public void accountNumberIsCreated(){
//        String accountNumber = Utilities.randomStringNumber();
//
//        assertThat(accountNumber,  is(not(empty())));
//        assertThat(accountNumber, CoreMatchers.containsString("VIN"));
//
//    }
//}
