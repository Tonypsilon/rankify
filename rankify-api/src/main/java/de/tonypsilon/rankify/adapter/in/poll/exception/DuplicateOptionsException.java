package de.tonypsilon.rankify.adapter.in.poll.exception;

public class DuplicateOptionsException extends RuntimeException {
    public DuplicateOptionsException() {
        super("Options must be pairwise distinct");
    }
}
