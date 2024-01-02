package com.example.demo.service;

import java.util.regex.Pattern;

public class InputValidator {

    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    public static boolean isValidInput(String input) {
        return input != null && ALPHANUMERIC_PATTERN.matcher(input).matches();
    }
}
