package de.tonypsilon.rankify.adapter.in.poll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tonypsilon.rankify.application.usecase.InitiatePollCommand;
import de.tonypsilon.rankify.domain.PollName;
import de.tonypsilon.rankify.domain.PollState;
import de.tonypsilon.rankify.infrastructure.exception.ErrorResponse;
import io.restassured.http.ContentType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractPollIntegrationTest {

    @LocalServerPort
    protected int port;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected void postPoll(InitiatePollCommand initiatePollCommand) {
        try {
            final var createPollResponse = given()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(initiatePollCommand))
                    .when()
                    .post("/polls")
                    .then()
                    .statusCode(201)
                    .extract()
                    .as(InitiatePollResponse.class);
            assertThat(createPollResponse).isNotNull();
            assertThat(createPollResponse.name()).isEqualTo(initiatePollCommand.name());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected PollResponse findPoll(PollName pollName) {
        return given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get("/polls/" + pollName.value())
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(PollResponse.class);
    }

    protected PollResponse changePollState(PollName pollName, PollState pollState) {
        try {
            System.out.println(objectMapper.writeValueAsString(new ChangePollStateCommand(pollState)));
            return given()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(new ChangePollStateCommand(pollState)))
                    .when()
                    .patch("/polls/" + pollName.value())
                    .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .as(PollResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected ErrorResponse changePollStateErrorResponse(PollName pollName, PollState pollState) {
        try {
            return given()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(new ChangePollStateCommand(pollState)))
                    .when()
                    .patch("/polls/" + pollName.value())
                    .then()
                    .statusCode(400)
                    .extract()
                    .response()
                    .as(ErrorResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void castBallot(PollName pollName, CastBallotCommand castBallotCommand) {
        try {
            given()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(castBallotCommand))
                    .when()
                    .patch("/polls/" + pollName.value() + "/votes")
                    .then()
                    .statusCode(204);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected ErrorResponse castBallotErrorResponse(PollName pollName, CastBallotCommand castBallotCommand) {
        try {
            return given()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(castBallotCommand))
                    .when()
                    .patch("/polls/" + pollName.value() + "/votes")
                    .then()
                    .statusCode(400)
                    .extract()
                    .response()
                    .as(ErrorResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
