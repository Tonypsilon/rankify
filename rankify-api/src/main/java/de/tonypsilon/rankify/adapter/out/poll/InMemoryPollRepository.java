package de.tonypsilon.rankify.adapter.out.poll;

import de.tonypsilon.rankify.domain.Poll;
import de.tonypsilon.rankify.domain.PollName;
import de.tonypsilon.rankify.domain.PollRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory implementation of the PollRepository interface.
 * Works with defensive copies of Poll objects so that modifications of Poll objects
 * don't affect the stored data.
 */
@Repository
public class InMemoryPollRepository implements PollRepository {

    private final Map<PollName, Poll> polls = new HashMap<>();

    @Override
    public Optional<Poll> findByName(PollName name) {
        return Optional.ofNullable(polls.get(name))
                .map(this::copyPoll);
    }

    @Override
    public PollName save(Poll poll) {
        polls.put(poll.name(), copyPoll(poll));
        return poll.name();
    }

    private Poll copyPoll(Poll poll) {
        return Poll.withNameOptionsAndState(poll.name(), new LinkedHashSet<>(poll.ballot().options()), poll.state());
    }
}
