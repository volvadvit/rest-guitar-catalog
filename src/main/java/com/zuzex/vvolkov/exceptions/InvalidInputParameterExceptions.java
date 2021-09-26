package com.zuzex.vvolkov.exceptions;

public class InvalidInputParameterExceptions extends RuntimeException {
    public InvalidInputParameterExceptions(String parameter) {
        super("Invalid input parameter: " + parameter);
    }
}