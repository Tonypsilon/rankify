package de.tonypsilon.rankify.adapter.exception;

public class DuplicateOptionsException extends RuntimeException {
    public DuplicateOptionsException() {
        super("Options must be pairwise distinct");
    }
}
