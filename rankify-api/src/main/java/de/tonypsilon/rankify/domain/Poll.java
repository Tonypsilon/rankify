package de.tonypsilon.rankify.domain;

import java.util.List;
import java.util.SequencedSet;

public class Poll {

    private final PollName name;
    private final SequencedSet<Option> options;

    private Poll(PollName name, SequencedSet<Option> options) {
        this.name = name;
        this.options = options;
    }

    public static Poll withNameAndOptions(PollName name, SequencedSet<Option> options) {
        return new Poll(name, options);
    }

    public PollName name() {
        return name;
    }

    public List<Option> options() {
        return List.copyOf(options);
    }
}
