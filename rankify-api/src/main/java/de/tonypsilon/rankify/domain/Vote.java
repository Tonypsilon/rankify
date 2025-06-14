package de.tonypsilon.rankify.domain;

import java.util.Collection;
import java.util.List;

public record Vote(Collection<Ranking> rankings) {

    public Vote {
        if (rankings == null || rankings.isEmpty()) {
            throw new IllegalArgumentException("Votes must not be null or empty");
        }
        if (rankings.stream().map(Ranking::option).distinct().count() != rankings.size()) {
            throw new DuplicateOptionsException();
        }
        rankings = List.copyOf(rankings);
    }

    /**
     * Return a defensive copy of the rankings.
     *
     * @return defensive copy of rankings
     */
    public Collection<Ranking> rankings() {
        return List.copyOf(rankings);
    }

}
