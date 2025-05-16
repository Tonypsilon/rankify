package de.tonypsilon.rankify.adapter.in;

import de.tonypsilon.rankify.infrastructure.exception.InvalidPollNameException;

record CreatePollCommand(String name) {
    public CreatePollCommand {
        if (name == null || name.isBlank()) {
            throw new InvalidPollNameException("Name cannot be null or blank");
        }
    }
}
