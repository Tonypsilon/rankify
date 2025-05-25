package de.tonypsilon.rankify.adapter.out.poll;

import de.tonypsilon.rankify.adapter.exception.PollAlreadyExistsException;
import de.tonypsilon.rankify.domain.Poll;
import de.tonypsilon.rankify.domain.PollName;
import de.tonypsilon.rankify.domain.PollRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryPollRepository implements PollRepository {

    private final Map<PollName, Poll> polls = new HashMap<>();

    @Override
    public Optional<Poll> findByName(PollName name) {
        return Optional.ofNullable(polls.get(name));
    }

    @Override
    public PollName save(Poll poll) {
        if (polls.containsKey(poll.name())) {
            throw new PollAlreadyExistsException(poll.name());
        }
        polls.put(poll.name(), poll);
        return poll.name();
    }

}
