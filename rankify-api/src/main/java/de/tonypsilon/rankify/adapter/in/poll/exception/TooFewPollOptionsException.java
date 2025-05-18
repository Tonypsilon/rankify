package de.tonypsilon.rankify.adapter.in.poll.exception;

public class TooFewPollOptionsException extends RuntimeException {
    public TooFewPollOptionsException() {
        super("At least two poll options are required");
    }
}
