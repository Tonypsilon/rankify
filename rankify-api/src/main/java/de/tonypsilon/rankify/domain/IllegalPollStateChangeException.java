package de.tonypsilon.rankify.domain;

public class IllegalPollStateChangeException extends RuntimeException {
    public IllegalPollStateChangeException(String message) {
        super(message);
    }
}
