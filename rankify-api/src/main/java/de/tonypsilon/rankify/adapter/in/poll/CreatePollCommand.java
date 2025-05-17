package de.tonypsilon.rankify.adapter.in.poll;

import de.tonypsilon.rankify.domain.PollName;

record CreatePollCommand(PollName name) {

    public static CreatePollCommand ofName(String name) {
        return new CreatePollCommand(new PollName(name));
    }

}
