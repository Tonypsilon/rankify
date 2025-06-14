package de.tonypsilon.rankify.domain;

import java.util.List;

public record Ballot(List<Option> options) {

    public Ballot {
        if (options == null || options.size() < 2) {
            throw new TooFewPollOptionsException();
        }
        if (options.stream().distinct().count() != options.size()) {
            throw new DuplicateOptionsException();
        }
    }

    /**
     * Return a defensive copy of the options.
     *
     * @return defensive copy of options
     */
    public List<Option> options() {
        return List.copyOf(options);
    }
}
