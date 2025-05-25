package de.tonypsilon.rankify.application.usecase;

import de.tonypsilon.rankify.domain.Poll;
import de.tonypsilon.rankify.domain.PollName;
import de.tonypsilon.rankify.domain.PollRepository;
import de.tonypsilon.rankify.infrastructure.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FindPollUsecase {

    private final PollRepository pollRepository;

    public FindPollUsecase(final PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    public Poll findPollByName(final PollName name) {
        return pollRepository.findByName(name)
                .orElseThrow(NotFoundException::new);
    }
}
