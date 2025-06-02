package de.tonypsilon.rankify.domain;

public record Ranking(Option option, int rank) {
    public Ranking {
        if (option == null) {
            throw new IllegalArgumentException("Option cannot be null");
        }
        if (rank < 1) {
            throw new IllegalArgumentException("Rank must be greater than 0");
        }
    }
}
