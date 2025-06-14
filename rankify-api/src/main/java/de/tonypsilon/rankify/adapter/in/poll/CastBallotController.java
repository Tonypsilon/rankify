package de.tonypsilon.rankify.adapter.in.poll;

import de.tonypsilon.rankify.application.usecase.CastBallotUsecase;
import de.tonypsilon.rankify.domain.PollName;
import de.tonypsilon.rankify.domain.Vote;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class CastBallotController {

    private final CastBallotUsecase castBallotUsecase;

    public CastBallotController(final CastBallotUsecase castBallotUsecase) {
        this.castBallotUsecase = castBallotUsecase;
    }

    @PatchMapping(value = "/polls/{pollName}/votes",
            consumes = "application/json")
    public ResponseEntity<Void> castBallot(@PathVariable final PollName pollName,
                                           @RequestBody final CastBallotCommand castBallotCommand) {
        Objects.requireNonNull(pollName);
        Objects.requireNonNull(castBallotCommand);
        castBallotUsecase.castBallot(pollName, new Vote(castBallotCommand.rankings()));
        return ResponseEntity.noContent().build();
    }
}
