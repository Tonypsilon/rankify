package de.tonypsilon.rankify.domain;

public class TooFewPollOptionsException extends RuntimeException {
    public TooFewPollOptionsException() {
        super("At least two poll options are required");
    }
}
