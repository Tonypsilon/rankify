package de.tonypsilon.rankify.adapter.in.poll;

import de.tonypsilon.rankify.domain.PollName;

public record CastBallotResponse(PollName pollName, boolean success) {
}
