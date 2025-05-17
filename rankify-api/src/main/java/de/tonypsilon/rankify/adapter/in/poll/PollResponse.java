package de.tonypsilon.rankify.adapter.in.poll;

import de.tonypsilon.rankify.domain.PollName;

record PollResponse(PollName name) {
    public static PollResponse ofName(String name) {
        return new PollResponse(new PollName(name));
    }

}
