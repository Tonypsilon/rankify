package de.tonypsilon.rankify.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PollControllerTest {

    @LocalServerPort
    private int port;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testPollCreationHappyPath() throws Exception {
        final String pollName = "Test Poll";
        final var createPollCommand = new CreatePollCommand(pollName);
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
        assertThat(postPollResponse.name()).isEqualTo(pollName);
    }
}
