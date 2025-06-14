package de.tonypsilon.rankify.adapter.in.poll;

import de.tonypsilon.rankify.application.usecase.InitiatePollCommand;
import de.tonypsilon.rankify.domain.Option;
import de.tonypsilon.rankify.domain.PollName;
import de.tonypsilon.rankify.domain.PollState;
import de.tonypsilon.rankify.domain.Ranking;
import de.tonypsilon.rankify.infrastructure.exception.ErrorResponse;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CastBallotIntegrationTest extends AbstractPollIntegrationTest {

    @Test
    void shouldCastBallot() {
        final var pollName = new PollName("Test_Poll_cast.ballot1");
        // Given a poll with three options
        postPoll(InitiatePollCommand.ofNameAndOptions(pollName,
                List.of(new Option("Option 1"), new Option("Option 2"), new Option("Option 3"))));

        // Activate the poll
        changePollState(pollName, PollState.ACTIVE);

        // When a ballot is cast
        List<Ranking> rankings = List.of(
                new Ranking(new Option("Option 1"), 1),
                new Ranking(new Option("Option 2"), 2),
                new Ranking(new Option("Option 3"), 3)
        );
        CastBallotCommand command = new CastBallotCommand(rankings);
        castBallot(pollName, command);
    }

    @Test
    void shouldNotCastBallotForNonExistentPoll() throws Exception {
        final var pollName = new PollName("Non_Existent_Poll");
        // When trying to cast a ballot for a non-existent poll
        List<Ranking> rankings = List.of(
                new Ranking(new Option("Option 1"), 1),
                new Ranking(new Option("Option 2"), 2)
        );
        CastBallotCommand command = new CastBallotCommand(rankings);

        // Then 404 Not Found should be returned
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(command))
                .when()
                .patch("/polls/" + pollName.value() + "/votes")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldNotCastBallotForInactivePoll() {
        final var pollName = new PollName("Test_Poll_inactive.ballot1");
        // Given a poll that is inactive
        postPoll(InitiatePollCommand.ofNameAndOptions(pollName,
                List.of(new Option("Option 1"), new Option("Option 2"))));
        // Ensure poll is in CREATED state (inactive)

        // When trying to cast a ballot for the inactive poll
        List<Ranking> rankings = List.of(
                new Ranking(new Option("Option 1"), 1),
                new Ranking(new Option("Option 2"), 2)
        );
        CastBallotCommand command = new CastBallotCommand(rankings);

        // Then an error should be returned
        ErrorResponse errorResponse = castBallotErrorResponse(pollName, command);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.message()).isEqualTo("Poll is not active");
    }

    @Test
    void shouldNotCastBallotForFinishedPoll() {
        final var pollName = new PollName("Test_Poll_finished.ballot1");
        // Given a poll that is finished
        postPoll(InitiatePollCommand.ofNameAndOptions(pollName,
                List.of(new Option("Option 1"), new Option("Option 2"))));
        changePollState(pollName, PollState.FINISHED);

        // When trying to cast a ballot for the finished poll
        List<Ranking> rankings = List.of(
                new Ranking(new Option("Option 1"), 1),
                new Ranking(new Option("Option 2"), 2)
        );
        CastBallotCommand command = new CastBallotCommand(rankings);

        // Then an error should be returned
        ErrorResponse errorResponse = castBallotErrorResponse(pollName, command);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.message()).isEqualTo("Poll is not active");
    }

    @Test
    void shouldNotCastBallotWithDuplicateOptions() {
        final var pollName = new PollName("Test_Poll_duplicate.ballot1");
        // Given a poll with two options
        postPoll(InitiatePollCommand.ofNameAndOptions(pollName,
                List.of(new Option("Option 1"), new Option("Option 2"))));
        // Activate the poll
        changePollState(pollName, PollState.ACTIVE);

        // When trying to cast a ballot with duplicate options
        List<Ranking> rankings = List.of(
                new Ranking(new Option("Option 1"), 1),
                new Ranking(new Option("Option 1"), 2)
        );
        CastBallotCommand command = new CastBallotCommand(rankings);

        // Then an error should be returned
        ErrorResponse errorResponse = castBallotErrorResponse(pollName, command);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.message()).isEqualTo("Options must be pairwise distinct");
    }
}
