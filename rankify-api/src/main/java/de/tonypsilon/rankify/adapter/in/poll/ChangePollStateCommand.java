package de.tonypsilon.rankify.adapter.in.poll;

import de.tonypsilon.rankify.domain.PollState;

public record ChangePollStateCommand(PollState state) {

    public ChangePollStateCommand {
        if (state == null) {
            throw new IllegalArgumentException("New state must not be null");
        }
    }
}
