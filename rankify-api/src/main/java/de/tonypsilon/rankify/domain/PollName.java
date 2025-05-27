package de.tonypsilon.rankify.domain;

import de.tonypsilon.rankify.adapter.in.poll.exception.InvalidPollNameException;

public record PollName(String value) {
    public PollName {
        if (value == null || value.isBlank()) {
            throw new InvalidPollNameException();
        }
        if (!value.matches("[a-zA-Z0-9._-]+")) {
            throw new InvalidPollNameException();
        }
    }

}
