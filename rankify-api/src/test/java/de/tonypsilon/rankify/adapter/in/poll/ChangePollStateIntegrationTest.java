package de.tonypsilon.rankify.adapter.in.poll;

import de.tonypsilon.rankify.application.usecase.CreatePollCommand;
import de.tonypsilon.rankify.domain.Option;
import de.tonypsilon.rankify.domain.PollName;
import de.tonypsilon.rankify.domain.PollState;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class ChangePollStateIntegrationTest extends AbstractPollIntegrationTest {

    @Test
    void shouldActivateInactivePoll() {
        // Given
        final var pollName = new PollName("Test_Poll1");
        postPoll(CreatePollCommand.ofNameAndOptions(
                pollName,
                List.of(new Option("Option 1"), new Option("Option 2")))
        );
        var pollResponse = findPoll(pollName);
        assertThat(pollResponse).isNotNull();

        // When
        var response = changePollState(pollName, PollState.ACTIVE);

        // Then
        assertThat(response.state()).isEqualTo(PollState.ACTIVE);
    }

    @Test
    void shouldActivateActivePoll() {
        // Given
        final var pollName = new PollName("Test_Poll2");
        postPoll(CreatePollCommand.ofNameAndOptions(
                pollName,
                List.of(new Option("Option 1"), new Option("Option 2")))
        );
        changePollState(pollName, PollState.ACTIVE);

        // When
        var response = changePollState(pollName, PollState.ACTIVE);

        // Then
        assertThat(response.state()).isEqualTo(PollState.ACTIVE);
    }

    @Test
    void shouldNotActivateFinishedPoll() {
        // Given
        final var pollName = new PollName("Test_Poll3");
        postPoll(CreatePollCommand.ofNameAndOptions(
                pollName,
                List.of(new Option("Option 1"), new Option("Option 2")))
        );
        changePollState(pollName, PollState.FINISHED);

        // When
        final var changePollStateErrorResponse = changePollStateErrorResponse(pollName, PollState.ACTIVE);

        // Then
        assertThat(changePollStateErrorResponse.message()).isEqualTo("Cannot activate a finished poll");

    }

    @Test
    void shouldDeactivateActivePoll() {
        // Given
        final var pollName = new PollName("Test_Poll4");
        postPoll(CreatePollCommand.ofNameAndOptions(
                pollName,
                List.of(new Option("Option 1"), new Option("Option 2")))
        );
        changePollState(pollName, PollState.ACTIVE);

        // When
        var response = changePollState(pollName, PollState.INACTIVE);

        // Then
        assertThat(response.state()).isEqualTo(PollState.INACTIVE);
    }

    @Test
    void shouldDeactivateInactivePoll() {
        // Given
        final var pollName = new PollName("Test_Poll5");
        postPoll(CreatePollCommand.ofNameAndOptions(
                pollName,
                List.of(new Option("Option 1"), new Option("Option 2")))
        );
        var pollResponse = findPoll(pollName);
        assertThat(pollResponse.state()).isEqualTo(PollState.INACTIVE);

        // When
        var response = changePollState(pollName, PollState.INACTIVE);

        // Then
        assertThat(response.state()).isEqualTo(PollState.INACTIVE);
    }

    @Test
    void shouldNotDeactivateFinishedPoll() {
        // Given
        final var pollName = new PollName("Test_Poll6");
        postPoll(CreatePollCommand.ofNameAndOptions(
                pollName,
                List.of(new Option("Option 1"), new Option("Option 2")))
        );
        changePollState(pollName, PollState.FINISHED);

        // When
        final var changePollStateErrorResponse = changePollStateErrorResponse(pollName, PollState.INACTIVE);

        // Then
        assertThat(changePollStateErrorResponse.message()).isEqualTo("Cannot deactivate a finished poll");
    }

    @Test
    void shouldFinishActivePoll() {
        // Given
        final var pollName = new PollName("Test_Poll7");
        postPoll(CreatePollCommand.ofNameAndOptions(
                pollName,
                List.of(new Option("Option 1"), new Option("Option 2")))
        );
        changePollState(pollName, PollState.ACTIVE);

        // When
        var response = changePollState(pollName, PollState.FINISHED);

        // Then
        assertThat(response.state()).isEqualTo(PollState.FINISHED);
    }

    @Test
    void shouldFinishInactivePoll() {
        // Given
        final var pollName = new PollName("Test_Poll8");
        postPoll(CreatePollCommand.ofNameAndOptions(
                pollName,
                List.of(new Option("Option 1"), new Option("Option 2")))
        );
        var pollResponse = findPoll(pollName);
        assertThat(pollResponse.state()).isEqualTo(PollState.INACTIVE);

        // When
        var response = changePollState(pollName, PollState.FINISHED);

        // Then
        assertThat(response.state()).isEqualTo(PollState.FINISHED);
    }

    @Test
    void shouldFinishFinishedPoll() {
        // Given
        final var pollName = new PollName("Test_Poll9");
        postPoll(CreatePollCommand.ofNameAndOptions(
                pollName,
                List.of(new Option("Option 1"), new Option("Option 2")))
        );
        changePollState(pollName, PollState.FINISHED);

        // When
        var response = changePollState(pollName, PollState.FINISHED);

        // Then
        assertThat(response.state()).isEqualTo(PollState.FINISHED);
    }

}
