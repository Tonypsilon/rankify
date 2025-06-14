package de.tonypsilon.rankify.domain;

import java.util.*;

public class Poll {

    private final PollName name;
    private final Ballot ballot;
    private PollState state = PollState.INACTIVE;
    private final Collection<Vote> votes = new ArrayList<>();

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

    /**
     * Casts a ballot in the poll.
     *
     * @param vote the vote containing rankings of options
     * @throws IllegalStateException    if the poll is not active
     * @throws IllegalArgumentException if the ballot contains options not in the poll
     */
    public void castBallot(Vote vote) {
        if (state != PollState.ACTIVE) {
            throw new PollNotActiveException();
        }
        if (!vote.rankings().stream().map(Ranking::option).allMatch(ballot.options()::contains)) {
            throw new InvalidOptionException();
        }
        votes.add(vote);
    }
}
