package de.tonypsilon.rankify.adapter.in.poll;

import de.tonypsilon.rankify.domain.Option;
import de.tonypsilon.rankify.domain.Poll;
import de.tonypsilon.rankify.domain.PollName;
import de.tonypsilon.rankify.domain.PollState;

import java.util.List;

record PollResponse(PollName name, List<Option> options, PollState state) {

    public static PollResponse ofPoll(Poll poll) {
        return new PollResponse(poll.name(), poll.options(), poll.state());
    }
}
