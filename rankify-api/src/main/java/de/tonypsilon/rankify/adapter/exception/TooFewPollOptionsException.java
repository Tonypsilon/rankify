package de.tonypsilon.rankify.adapter.exception;

public class TooFewPollOptionsException extends RuntimeException {
    public TooFewPollOptionsException() {
        super("At least two poll options are required");
    }
}
