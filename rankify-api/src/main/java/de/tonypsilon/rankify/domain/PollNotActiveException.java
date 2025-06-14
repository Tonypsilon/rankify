package de.tonypsilon.rankify.domain;

public class PollNotActiveException extends RuntimeException {
    public PollNotActiveException() {
        super("Poll is not active");
    }
}
