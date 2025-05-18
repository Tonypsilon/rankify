package de.tonypsilon.rankify.adapter.in.poll;

import de.tonypsilon.rankify.adapter.in.poll.exception.DuplicateOptionsException;
import de.tonypsilon.rankify.adapter.in.poll.exception.TooFewPollOptionsException;
import de.tonypsilon.rankify.domain.PollName;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.SequencedSet;

record CreatePollCommand(PollName name, SequencedSet<Option> options) {

    public static CreatePollCommand ofNameAndOptions(String name, Collection<Option> optionsCollection) {
        var options = Collections.unmodifiableSequencedSet(new LinkedHashSet<Option>(optionsCollection));
        if (options.size() < 2) {
            throw new TooFewPollOptionsException();
        }
        if (options.size() < optionsCollection.size()) {
            throw new DuplicateOptionsException();
        }
        return new CreatePollCommand(new PollName(name), options);
    }

}
