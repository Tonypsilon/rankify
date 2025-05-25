package de.tonypsilon.rankify.domain;

import java.util.Optional;

public interface PollRepository {

    Optional<Poll> findByName(PollName name);

    PollName save(Poll poll);
}
