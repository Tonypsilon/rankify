package de.tonypsilon.rankify.domain;

public class NoRankingsException extends RuntimeException {
    public NoRankingsException() {
        super("At least one ranking must be provided");
    }
}
