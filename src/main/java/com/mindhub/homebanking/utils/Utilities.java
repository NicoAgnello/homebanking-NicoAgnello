package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;

public class Utilities {
    public static String randomNumberAccount(AccountRepository accountRepository){
        String newNumber;
        Boolean numberBol;
        do {
            newNumber = randomStringNumber();
            numberBol = accountRepository.existsAccountByNumber(newNumber);
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
    public static String randomNumberCard(CardRepository cardRepository){
        String newNumber;
        Boolean cardOptional;
        do {
            newNumber= randomString();
            cardOptional= cardRepository.existsCardByNumber(newNumber);
        }while(cardOptional);
        return newNumber;
    }
    public static String randomString(){
        int number1_1 = (int) (Math.random() * (5- 4+ 1)+4);
        int number1 = (int) (Math.random() * (999 - 100  + 1) + 100);
        int number2 = (int) (Math.random() * (9999 - 1000  + 1) + 1000);
        int number3 = (int) (Math.random() * (9999 - 1000  + 1) + 1000);
        int number4 = (int) (Math.random() * (9999 - 1000  + 1) + 1000);
        return  number1_1+""+number1+"-"+number2+"-"+number3+"-"+number4;
    }

}
