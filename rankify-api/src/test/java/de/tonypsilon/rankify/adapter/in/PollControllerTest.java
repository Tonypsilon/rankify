package de.tonypsilon.rankify.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.tonypsilon.rankify.infrastructure.exception.ErrorResponse;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PollControllerTest {

    @LocalServerPort
    private int port;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateTwoPollsWithDifferentNames() throws Exception {
        final String[] pollNames = {"Test Poll 1", "Test Poll 2"};
        final var createPollCommands = List
                .of(new CreatePollCommand(pollNames[0]), new CreatePollCommand(pollNames[1]));
        for (var createPollCommand : createPollCommands) {
            final var postPollResponse = given()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(createPollCommand))
                    .when()
                    .post("/polls")
                    .then()
                    .statusCode(201)
                    .extract()
                    .as(CreatePollResponse.class);
            assertThat(postPollResponse).isNotNull();
            assertThat(postPollResponse.name()).isEqualTo(createPollCommand.name());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void shouldNotCreatePollWithInvalidName(final String pollName) throws Exception {
        final var createPollCommand = new InvalidCreatePollCommand(pollName);
        final var postPollResponse = given()
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
        assertThat(postPollResponse.message()).isEqualTo("Invalid poll name provided");
    }

    private record InvalidCreatePollCommand(String name) {
    }
}
