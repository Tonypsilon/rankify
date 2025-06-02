package de.tonypsilon.rankify.adapter.in.poll;

import de.tonypsilon.rankify.domain.Ranking;

import java.util.Collection;
import java.util.Collections;

public record CastBallotCommand(Collection<Ranking> rankings) {
    public CastBallotCommand {
        if (rankings == null || rankings.isEmpty()) {
            throw new IllegalArgumentException("Votes must not be null or empty");
        }
        rankings = Collections.unmodifiableCollection(rankings);
    }
}
