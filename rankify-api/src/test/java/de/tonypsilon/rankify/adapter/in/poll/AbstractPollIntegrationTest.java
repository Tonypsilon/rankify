package de.tonypsilon.rankify.adapter.in.poll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tonypsilon.rankify.application.usecase.CreatePollCommand;
import de.tonypsilon.rankify.domain.PollName;
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

    protected void postPoll(CreatePollCommand createPollCommand) {
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
}
