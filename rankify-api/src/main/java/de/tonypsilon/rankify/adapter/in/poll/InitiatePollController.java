package de.tonypsilon.rankify.adapter.in.poll;

import de.tonypsilon.rankify.adapter.in.poll.exception.InvalidPollNameException;
import de.tonypsilon.rankify.application.usecase.CreatePollUsecase;
import de.tonypsilon.rankify.application.usecase.InitiatePollCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/polls")
class InitiatePollController {

    private final CreatePollUsecase createPollUsecase;

    InitiatePollController(final CreatePollUsecase createPollUsecase) {
        this.createPollUsecase = createPollUsecase;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<InitiatePollResponse> createPoll(RequestEntity<InitiatePollCommand> commandRequestEntity) {
        final var command = commandRequestEntity.getBody();
        if (command == null) {
            throw new InvalidPollNameException();
        }
        var pollName = createPollUsecase.createPoll(command);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new InitiatePollResponse(pollName));
    }
}
