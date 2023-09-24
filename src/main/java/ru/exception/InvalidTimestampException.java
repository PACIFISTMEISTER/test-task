package ru.exception;

public class InvalidTimestampException extends Exception {

    private InvalidTimestampException(String message) {
        super(message);
    }

    public static InvalidTimestampException createExceptionByTimestamp(int timestamp) {
        return new InvalidTimestampException("invalid timestamp cant be less then zero " + timestamp);
    }
}
