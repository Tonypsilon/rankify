package de.tonypsilon.rankify.adapter.in.poll;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/polls")
class GetPollController {

    @GetMapping(value = "/{pollName}", produces = "application/json")
    ResponseEntity<PollResponse> getPoll(@PathVariable String pollName) {
        return ResponseEntity.ok(PollResponse.ofName(pollName));

    }
}
