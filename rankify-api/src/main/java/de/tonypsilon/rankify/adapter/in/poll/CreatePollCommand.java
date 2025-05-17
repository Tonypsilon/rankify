package de.tonypsilon.rankify.adapter.in.poll;

import de.tonypsilon.rankify.adapter.in.poll.exception.InvalidPollNameException;

record CreatePollCommand(String name) {
    public CreatePollCommand {
        if (name == null || name.isBlank()) {
            throw new InvalidPollNameException("Name cannot be null or blank");
        }
    }
}
