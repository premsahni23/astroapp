package com.dekhokaun.mindarobackend.utils;

import lombok.Data;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class RegexUtils {

    private static final Map<String, Integer> COUNTRY_PHONE_LENGTHS = Map.of(
            "IN", 10,
            "US", 10,
            "UK", 10,
            "CA", 10,
            "AU", 9
    );

    public static boolean isValidUsername(String name) {

        String regex = "^[A-Za-z, ]++$";

        Pattern p = Pattern.compile(regex);

        if (name == null) {
            return false;
        }

        Matcher m = p.matcher(name);

        return m.matches();
    }

    public static boolean isValidEmail(String email) {

        String regex = "^(.+)@(.+)$";

        Pattern p = Pattern.compile(regex);

        if (email == null) {
            return false;
        }

        Matcher m = p.matcher(email);

        return m.matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber, String countryCode) {
        if (phoneNumber == null || countryCode == null) {
            return false;
        }
        Integer expectedLength = COUNTRY_PHONE_LENGTHS.get(countryCode.toUpperCase());
        return expectedLength != null && phoneNumber.length() == expectedLength;
    }

}