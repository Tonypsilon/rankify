package de.tonypsilon.rankify.adapter.in.poll;

import de.tonypsilon.rankify.application.usecase.InitiatePollCommand;
import de.tonypsilon.rankify.domain.Option;
import de.tonypsilon.rankify.domain.PollName;
import de.tonypsilon.rankify.domain.PollState;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CastBallotIntegrationTest extends AbstractPollIntegrationTest {

    @Test
    void shouldCastBallot() {
        final var pollName = new PollName("Test_Poll_cast.ballot1");
        // Given a poll with three options
        postPoll(InitiatePollCommand.ofNameAndOptions(pollName,
                List.of(new Option("Option 1"), new Option("Option 2"), new Option("Option 3"))));

        // When a ballot is cast

        // Then the ballot should be recorded correctly

    }

    @Test
    void shouldNotCastBallotForNonExistentPoll() {
        final var pollName = new PollName("Non_Existent_Poll");
        // When trying to cast a ballot for a non-existent poll

        // Then an error should be returned
    }

    @Test
    void shouldNotCastBallotForInactivePoll() {
        final var pollName = new PollName("Test_Poll_inactive.ballot1");
        // Given a poll that is inactive
        postPoll(InitiatePollCommand.ofNameAndOptions(pollName,
                List.of(new Option("Option 1"), new Option("Option 2"))));

        // When trying to cast a ballot for the inactive poll

        // Then an error should be returned
    }

    @Test
    void shouldNotCastBallotForFinishedPoll() {
        final var pollName = new PollName("Test_Poll_finished.ballot1");
        // Given a poll that is finished
        postPoll(InitiatePollCommand.ofNameAndOptions(pollName,
                List.of(new Option("Option 1"), new Option("Option 2"))));
        changePollState(pollName, PollState.FINISHED);

        // When trying to cast a ballot for the finished poll

        // Then an error should be returned
    }

    @Test
    void shouldNotCastBallotWithDuplicateOptions() {
        final var pollName = new PollName("Test_Poll_duplicate.ballot1");
        // Given a poll with two options
        postPoll(InitiatePollCommand.ofNameAndOptions(pollName,
                List.of(new Option("Option 1"), new Option("Option 2"))));

        // When trying to cast a ballot with duplicate options

        // Then an error should be returned
    }
}
