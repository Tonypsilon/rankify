package de.tonypsilon.rankify.adapter.in;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/polls")
class CreatePollController {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CreatePollResponse> createPoll(RequestEntity<CreatePollCommand> commandRequestEntity) {
        final var command = commandRequestEntity.getBody();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreatePollResponse(command.name()));
    }
}
