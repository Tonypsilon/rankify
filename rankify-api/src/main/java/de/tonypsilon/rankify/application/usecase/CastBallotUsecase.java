package de.tonypsilon.rankify.application.usecase;

import de.tonypsilon.rankify.domain.PollName;
import de.tonypsilon.rankify.domain.PollRepository;
import de.tonypsilon.rankify.domain.Vote;
import de.tonypsilon.rankify.infrastructure.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CastBallotUsecase {

    private final PollRepository pollRepository;

    public CastBallotUsecase(final PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    public void castBallot(PollName pollName, Vote vote) {
        var poll = pollRepository.findByName(pollName)
                .orElseThrow(NotFoundException::new);
        poll.castBallot(vote);
        pollRepository.save(poll);
    }
}
