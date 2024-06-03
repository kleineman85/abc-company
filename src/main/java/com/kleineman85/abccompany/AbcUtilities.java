package com.kleineman85.abccompany;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;

/**
 * Utility class, final because it should not be subclassed.
 * Constructor is private because it should not be instantiated.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AbcUtilities {

    public static String generatePassword() {
        SecureRandom secureRandom = new SecureRandom();
        int randomNumber = 1000 + secureRandom.nextInt(9000);

        // todo hash password
        return "Password" + randomNumber;
    }

    public static String generateDutchIban() {
        String countryCode = "NL";
        String checkDigits = "99";
        String bankcode = "ABNA";
        String accountNumber = "";

        return "TODO";
    }

}
