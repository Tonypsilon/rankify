package de.tonypsilon.rankify.application.usecase;

import de.tonypsilon.rankify.adapter.in.poll.exception.DuplicateOptionsException;
import de.tonypsilon.rankify.domain.Option;
import de.tonypsilon.rankify.domain.Poll;
import de.tonypsilon.rankify.domain.PollName;
import de.tonypsilon.rankify.domain.TooFewPollOptionsException;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SequencedSet;

public record CreatePollCommand(PollName name, SequencedSet<Option> options) {

    public static CreatePollCommand ofNameAndOptions(PollName name, List<Option> optionsCollection) {
        var options = Collections.unmodifiableSequencedSet(new LinkedHashSet<>(optionsCollection));
        if (options.size() < 2) {
            throw new TooFewPollOptionsException();
        }
        if (options.size() < optionsCollection.size()) {
            throw new DuplicateOptionsException();
        }
        return new CreatePollCommand(name, options);
    }

    public Poll toPoll() {
        return Poll.withNameAndOptions(name, options);
    }

}
