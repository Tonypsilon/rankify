package de.tonypsilon.rankify.adapter.in.poll;

import de.tonypsilon.rankify.application.usecase.ActivatePollUsecase;
import de.tonypsilon.rankify.application.usecase.DeactivatePollUsecase;
import de.tonypsilon.rankify.application.usecase.FinishPollUsecase;
import de.tonypsilon.rankify.domain.PollName;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/polls")
class ChangePollStateController {

    private final ActivatePollUsecase activatePollUsecase;
    private final DeactivatePollUsecase deactivatePollUsecase;
    private final FinishPollUsecase finishPollUsecase;

    ChangePollStateController(final ActivatePollUsecase activatePollUsecase,
                              final DeactivatePollUsecase deactivatePollUsecase,
                              final FinishPollUsecase finishPollUsecase) {
        this.activatePollUsecase = activatePollUsecase;
        this.deactivatePollUsecase = deactivatePollUsecase;
        this.finishPollUsecase = finishPollUsecase;
    }

    @PatchMapping(value = "/{pollNamePathVariable}",
            consumes = "application/json",
            produces = "application/json")
    ResponseEntity<PollResponse> changePollState(RequestEntity<ChangePollStateCommand> changeCommandRequestBody,
                                                 @PathVariable final String pollNamePathVariable) {
        var pollName = new PollName(Objects.requireNonNull(pollNamePathVariable));
        var patchedPoll = switch (Objects.requireNonNull(changeCommandRequestBody.getBody()).state()) {
            case ACTIVE -> PollResponse.ofPoll(activatePollUsecase.activatePoll(pollName));
            case INACTIVE -> PollResponse.ofPoll(deactivatePollUsecase.deactivatePoll(pollName));
            case FINISHED -> PollResponse.ofPoll(finishPollUsecase.finishPoll(pollName));
        };
        return ResponseEntity.ok(patchedPoll);
    }
}
