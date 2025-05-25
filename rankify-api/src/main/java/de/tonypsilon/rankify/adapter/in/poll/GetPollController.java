package de.tonypsilon.rankify.adapter.in.poll;

import de.tonypsilon.rankify.application.usecase.FindPollUsecase;
import de.tonypsilon.rankify.domain.PollName;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/polls")
class GetPollController {

    final FindPollUsecase findPollUsecase;

    GetPollController(final FindPollUsecase findPollUsecase) {
        this.findPollUsecase = findPollUsecase;
    }

    @GetMapping(value = "/{pollName}", produces = "application/json")
    ResponseEntity<PollResponse> getPollByName(@PathVariable String pollName) {
        PollName name = new PollName(Objects.requireNonNull(pollName));
        return ResponseEntity.ok(
                PollResponse.ofPoll(findPollUsecase.findPollByName(name))
        );
    }
}
