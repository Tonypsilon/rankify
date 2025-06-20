package de.tonypsilon.rankify.adapter.in.poll;

import de.tonypsilon.rankify.application.usecase.InitiatePollCommand;
import de.tonypsilon.rankify.domain.Option;
import de.tonypsilon.rankify.domain.PollName;
import de.tonypsilon.rankify.domain.PollState;
import de.tonypsilon.rankify.infrastructure.exception.ErrorResponse;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests basic functionality of the Poll API, such as creating polls,
 * validating poll names, and retrieving polls.
 */
class BasicPollIntegrationTest extends AbstractPollIntegrationTest {

    @Test
    void shouldCreateTwoPollsWithDifferentNames() {
        postPoll(createPollCommandOfNameAndBallot(new PollName("Test-1"), "Option 1", "Option 2"));
        postPoll(createPollCommandOfNameAndBallot(new PollName("Test-2"), "Option 1", "Option 2"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void shouldNotCreatePollWithInvalidName(final String pollName) throws Exception {
        final var createPollCommand = InvalidCreatePollCommand.of(pollName);
        final var postPollErrorResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(createPollCommand))
                .when()
                .post("/polls")
                .then()
                .statusCode(400)
                .extract()
                .response()
                .as(ErrorResponse.class);
        assertThat(postPollErrorResponse.message()).isEqualTo("Invalid poll name provided");
    }

    private record InvalidCreatePollCommand(InvalidPollName name) {
        private static InvalidCreatePollCommand of(String name) {
            return new InvalidCreatePollCommand(new InvalidPollName(name));
        }
    }

    private record InvalidPollName(String name) {
    }

    @Test
    void shouldNotCreatePollWithNoName() {
        final var postPollErrorResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .post("/polls")
                .then()
                .statusCode(400)
                .extract()
                .response()
                .as(ErrorResponse.class);
        assertThat(postPollErrorResponse.message()).isEqualTo("Invalid poll name provided");
    }

    @Test
    void shouldNotCreateTwoPollsWithSameName() throws Exception {
        final var pollName = new PollName("Test_Poll.1");
        postPoll(createPollCommandOfNameAndBallot(pollName, "Option 1", "Option 2"));
        final var postPollErrorResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(createPollCommandOfNameAndBallot(pollName, "Option 3", "Option 4")))
                .when()
                .post("/polls")
                .then()
                .statusCode(400)
                .extract()
                .response()
                .as(ErrorResponse.class);
        assertThat(postPollErrorResponse.message()).isEqualTo("Poll with name Test_Poll.1 already exists");
    }

    @Test
    void shouldFindCreatedPoll() {
        final var pollName = new PollName("Test-Poll.1");
        final var createPollCommand = createPollCommandOfNameAndBallot(pollName, "Option 1", "Option 2");
        postPoll(createPollCommand);
        final var pollResponse = findPoll(pollName);
        assertThat(pollResponse.name()).isEqualTo(pollName);
        assertThat(pollResponse.ballot().options()).containsExactly(new Option("Option 1"), new Option("Option 2"));
        assertThat(pollResponse.state()).isEqualTo(PollState.INACTIVE);
    }

    @Test
    void shouldNotFindPollWithoutName() {
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get("/polls/")
                .then()
                .statusCode(404);
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "\t", "\n"})
    void shouldNotFindPollWithInvalidName(String invalidPollName) {
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get("/polls/" + invalidPollName)
                .then()
                .statusCode(404);
    }

    private InitiatePollCommand createPollCommandOfNameAndBallot(PollName name, String... options) {
        return InitiatePollCommand
                .ofNameAndOptions(name, Arrays.stream(options).map(Option::new).toList());
    }
}
