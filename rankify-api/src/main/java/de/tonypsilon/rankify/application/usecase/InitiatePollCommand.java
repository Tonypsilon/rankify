package de.tonypsilon.rankify.application.usecase;

import de.tonypsilon.rankify.domain.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SequencedSet;

public record InitiatePollCommand(PollName name, SequencedSet<Option> options) {

    public static InitiatePollCommand ofNameAndOptions(PollName name, List<Option> optionsCollection) {
        var options = Collections.unmodifiableSequencedSet(new LinkedHashSet<>(optionsCollection));
        if (options.size() < 2) {
            throw new TooFewPollOptionsException();
        }
        if (options.size() < optionsCollection.size()) {
            throw new DuplicateOptionsException();
        }
        return new InitiatePollCommand(name, options);
    }

    public Poll toPoll() {
        return Poll.withNameAndOptions(name, options);
    }

}
