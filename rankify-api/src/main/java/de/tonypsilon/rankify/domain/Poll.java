package de.tonypsilon.rankify.domain;

import java.util.List;
import java.util.Objects;
import java.util.SequencedSet;

public class Poll {

    private final PollName name;
    private final Ballot ballot;
    private PollState state = PollState.INACTIVE;

    private Poll(PollName name, SequencedSet<Option> options) {
        this.name = name;
        this.ballot = new Ballot(List.copyOf(options));
    }

    private Poll(PollName name, SequencedSet<Option> options, PollState state) {
        this.name = name;
        this.ballot = new Ballot(List.copyOf(options));
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

    public Ballot ballot() {
        return ballot;
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
