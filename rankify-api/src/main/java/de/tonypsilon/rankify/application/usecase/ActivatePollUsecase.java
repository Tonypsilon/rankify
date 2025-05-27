package de.tonypsilon.rankify.application.usecase;

import de.tonypsilon.rankify.domain.Poll;
import de.tonypsilon.rankify.domain.PollName;
import de.tonypsilon.rankify.domain.PollRepository;
import de.tonypsilon.rankify.infrastructure.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ActivatePollUsecase {

    private final PollRepository pollRepository;

    public ActivatePollUsecase(final PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    public Poll activatePoll(final PollName pollName) {
        var poll = pollRepository.findByName(pollName)
                .orElseThrow(NotFoundException::new);
        poll.activate();
        return poll;
    }
}
