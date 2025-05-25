package de.tonypsilon.rankify.domain;

import java.util.List;
import java.util.Objects;
import java.util.SequencedSet;

public class Poll {

    private final PollName name;
    private final SequencedSet<Option> options;
    private PollState state = PollState.INACTIVE;

    private Poll(PollName name, SequencedSet<Option> options) {
        this.name = name;
        this.options = options;
    }

    private Poll(PollName name, SequencedSet<Option> options, PollState state) {
        this.name = name;
        this.options = options;
        this.state = state;
    }

    public static Poll withNameAndOptions(final PollName name,
                                          final SequencedSet<Option> options) {
        return new Poll(name, options);
    }

    public static Poll withNameOptionsAndState(final PollName name,
                                               final SequencedSet<Option> options,
                                               final PollState state) {
        return new Poll(name, options, Objects.requireNonNull(state));
    }

    public PollName name() {
        return name;
    }

    public List<Option> options() {
        return List.copyOf(options);
    }

    public boolean isActive() {
        return state == PollState.ACTIVE;
    }
}
