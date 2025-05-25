package de.tonypsilon.rankify.adapter.in.poll;

import de.tonypsilon.rankify.domain.Option;
import de.tonypsilon.rankify.domain.Poll;
import de.tonypsilon.rankify.domain.PollName;

import java.util.List;

record PollResponse(PollName name, List<Option> options) {

    public static PollResponse ofNameAndOptions(String name, List<Option> optionsCollection) {
        return new PollResponse(new PollName(name),
                List.copyOf(optionsCollection));
    }

    public static PollResponse ofPoll(Poll poll) {
        return new PollResponse(poll.name(), poll.options());
    }
}
