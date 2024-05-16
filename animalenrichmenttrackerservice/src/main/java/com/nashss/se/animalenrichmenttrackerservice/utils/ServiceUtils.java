package com.nashss.se.animalenrichmenttrackerservice.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Service utility to validate strings and generate Ids for habitats.
 */
public class ServiceUtils {
    private static final Pattern INVALID_CHARACTER_PATTERN = Pattern.compile("[\"'\\\\]");
    private static final int HABITAT_ID_LENGTH = 5;

    private ServiceUtils() {

    }

    /**
     * validates input string based on invalid character pattern constant.
     *
     * @param toValidate string to validate
     * @return boolean based on string validation
     */
    public static boolean isValidString(String toValidate) {
        if (StringUtils.isBlank(toValidate)) {
            return false;
        } else {
            return !INVALID_CHARACTER_PATTERN.matcher(toValidate).find();
        }
    }

    /**
     * generates random habitat Id.
     *
     * @return random string with length of 5 to represent the new habitat Id
     */
    public static String generateId() {
        return RandomStringUtils.randomAlphanumeric(HABITAT_ID_LENGTH);
    }
}
