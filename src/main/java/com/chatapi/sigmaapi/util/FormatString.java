package com.chatapi.sigmaapi
.util;

import java.util.Random;

public class FormatString {
    public static String customPhoneVNRegion(String phone) {
        String phoneRegister = phone;
        if (phone == null) {
            return null;
        }
        if (phone.charAt(0) == '0') {
            phoneRegister = phone.replaceFirst("0", "+84");
        }
        return phoneRegister;
    }

    public static String getRandomNumberOtp() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public static boolean getAccountTest(String acc) {
        if (acc.substring(0, 4).equals("0707")) {
            return true;
        }
        return false;
    }

    public static String filterSQLMiddle(String value){
        return "%" + value + "%";
    }
}
