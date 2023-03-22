package com.mindhub.homebanking.utils;

//import com.mindhub.homebanking.repositories.AccountRepository;
//import com.mindhub.homebanking.repositories.CardRepository;

import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ServicesImplementation.AccountServiceImpl;
import com.mindhub.homebanking.services.ServicesImplementation.CardServiceImpl;

public class Utilities {
    public static String randomNumberAccount(AccountService accountService){
        String newNumber;
        Boolean numberBol;
        do {
            newNumber = randomStringNumber();
            numberBol = accountService.existsAccountByNumber(newNumber);
        }while (numberBol);
        return newNumber;
    }
    public static String randomStringNumber (){
        String vin = "VIN";
        int number = (int) ((Math.random()*(99999999-1))+1);
        return vin+String.format("%03d",number);
    }
    public static int returnCvvNumber(){
        int number;
        number= (int) (Math.floor(Math.random() * (999-100)) + 100);
        return number;
    }
    public static String randomNumberCard(CardService cardService){
        String newNumber;
        Boolean cardOptional;
        do {
            newNumber= randomString();
            cardOptional= cardService.existsCardByNumber(newNumber);
        }while(cardOptional);
        return newNumber;
    }
    public static String randomString(){
        int number1_1 = (int) (Math.random() * (5- 4)+4);
        int number1 = (int) (Math.random() * (999 - 100) + 100);
        int number2 = (int) (Math.random() * (9999 - 1000) + 1000);
        int number3 = (int) (Math.random() * (9999 - 1000) + 1000);
        int number4 = (int) (Math.random() * (9999 - 1000) + 1000);
        return  number1_1+""+number1+"-"+number2+"-"+number3+"-"+number4;
    }

}
