package com.kleineman85.abccompany;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbcUtilitiesTest {

    @Test
    void givenPasswordPatternShouldReturnValidPassword() {
        // given
        Pattern pattern = Pattern.compile("^Password\\d{4}$");

        // when
        String result = AbcUtilities.generatePassword();

        // then
        assertNotNull(result, "password not generated");
        assertTrue(pattern.matcher(result).matches(), "password doesn't match format");

    }

    // todo
    @Test
    @Disabled
    void shouldReturnValidDutchIban() {
        // given
        Pattern ibanPattern = Pattern.compile("^NL\\d{2}[A-Z]{4}\\d{10}$");

        // when
//        String result = AbcUtilities.generateIban();
        String result = "NL02ABNA0123456789";

        // then
        assertNotNull(result, "IBAN not generated");
        assertTrue(ibanPattern.matcher(result).matches(), "iban doesn't match format");

    }

}