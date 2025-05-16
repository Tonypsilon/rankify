package de.tonypsilon.rankify.infrastructure.exception;

public class InvalidPollNameException extends RuntimeException {
    public InvalidPollNameException(String message) {
        super(message);
    }
}
