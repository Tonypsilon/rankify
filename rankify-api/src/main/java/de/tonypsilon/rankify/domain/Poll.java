package de.tonypsilon.rankify.domain;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.SequencedSet;

public class Poll {

    private final PollName name;
    private final SequencedSet<Option> options;
    private PollState state = PollState.INACTIVE;

    private Poll(PollName name, SequencedSet<Option> options) {
        this.name = name;
        if (options.size() < 2) {
            throw new TooFewPollOptionsException();
        }
        this.options = new LinkedHashSet<>(options);
    }

    private Poll(PollName name, SequencedSet<Option> options, PollState state) {
        this.name = name;
        if (options.size() < 2) {
            throw new TooFewPollOptionsException();
        }
        this.options = new LinkedHashSet<>(options);
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

    public PollState state() {
        return state;
    }

    public void activate() {
        if (state == PollState.FINISHED) {
            throw new IllegalPollStateChangeException("Cannot activate a finished poll");
        }
        state = PollState.ACTIVE;
    }

    public void deactivate() {
        if (state == PollState.FINISHED) {
            throw new IllegalPollStateChangeException("Cannot deactivate a finished poll");
        }
        state = PollState.INACTIVE;
    }

    public void finish() {
        state = PollState.FINISHED;
    }
}
