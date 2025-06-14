package de.tonypsilon.rankify.domain;

public class InvalidOptionException extends RuntimeException {
    public InvalidOptionException() {
        super("The provided option does not exist in the poll");
    }
}
