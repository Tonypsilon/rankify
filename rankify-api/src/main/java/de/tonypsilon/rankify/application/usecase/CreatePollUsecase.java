package de.tonypsilon.rankify.application.usecase;

import de.tonypsilon.rankify.domain.PollName;
import de.tonypsilon.rankify.domain.PollRepository;
import org.springframework.stereotype.Service;

@Service
public class CreatePollUsecase {

    private final PollRepository pollRepository;

    public CreatePollUsecase(final PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    public PollName createPoll(final CreatePollCommand command) {
        return pollRepository.save(command.toPoll());
    }
}
