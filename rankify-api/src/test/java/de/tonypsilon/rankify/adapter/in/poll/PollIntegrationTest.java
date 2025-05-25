package de.tonypsilon.rankify.adapter.in.poll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tonypsilon.rankify.application.usecase.CreatePollCommand;
import de.tonypsilon.rankify.domain.Option;
import de.tonypsilon.rankify.domain.PollName;
import de.tonypsilon.rankify.domain.PollState;
import de.tonypsilon.rankify.infrastructure.exception.ErrorResponse;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PollIntegrationTest {

    @LocalServerPort
    private int port;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateTwoPollsWithDifferentNames() {
        postPoll(createPollCommandOfNameAndOptions("Test-1", "Option 1", "Option 2"));
        postPoll(createPollCommandOfNameAndOptions("Test-2", "Option 1", "Option 2"));
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
    void shouldFindCreatedPoll() {
        final var pollName = "Test_Poll.1";
        final var createPollCommand = createPollCommandOfNameAndOptions(pollName, "Option 1", "Option 2");
        postPoll(createPollCommand);
        final var pollResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get("/polls/" + pollName)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(PollResponse.class);
        assertThat(pollResponse.name()).isEqualTo(new PollName(pollName));
        assertThat(pollResponse.options()).containsExactly(new Option("Option 1"), new Option("Option 2"));
        assertThat(pollResponse.state()).isEqualTo(PollState.INACTIVE);
    }

    @Test
    void shouldNotFindPollWithoutName() throws Exception {
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
    void shouldNotFindPollWithInvalidName(String invalidPollName) throws Exception {
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get("/polls/" + invalidPollName)
                .then()
                .statusCode(404);
    }

    private void postPoll(CreatePollCommand createPollCommand) {
        try {
            final var createPollResponse = given()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(createPollCommand))
                    .when()
                    .post("/polls")
                    .then()
                    .statusCode(201)
                    .extract()
                    .as(CreatePollResponse.class);
            assertThat(createPollResponse).isNotNull();
            assertThat(createPollResponse.name()).isEqualTo(createPollCommand.name());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private CreatePollCommand createPollCommandOfNameAndOptions(String name, String... options) {
        return CreatePollCommand
                .ofNameAndOptions(name, Arrays.stream(options).map(Option::new).toList());
    }
}
