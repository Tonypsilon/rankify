package de.tonypsilon.rankify.adapter.exception;

public class InvalidPollNameException extends RuntimeException {
    public InvalidPollNameException() {
        super("Invalid poll name provided");
    }
}
