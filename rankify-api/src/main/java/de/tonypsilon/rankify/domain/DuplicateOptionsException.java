package de.tonypsilon.rankify.domain;

public class DuplicateOptionsException extends RuntimeException {
    public DuplicateOptionsException() {
        super("Options must be pairwise distinct");
    }
}
