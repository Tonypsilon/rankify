package de.tonypsilon.rankify.adapter.in.poll.exception;

public class InvalidPollNameException extends RuntimeException {
    public InvalidPollNameException() {
        super("Invalid poll name provided");
    }
}
