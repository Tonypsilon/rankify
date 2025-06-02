package de.tonypsilon.rankify.adapter.in.poll;

import de.tonypsilon.rankify.domain.Ballot;
import de.tonypsilon.rankify.domain.Poll;
import de.tonypsilon.rankify.domain.PollName;
import de.tonypsilon.rankify.domain.PollState;

record PollResponse(PollName name, Ballot ballot, PollState state) {

    public static PollResponse ofPoll(Poll poll) {
        return new PollResponse(poll.name(), poll.ballot(), poll.state());
    }
}
