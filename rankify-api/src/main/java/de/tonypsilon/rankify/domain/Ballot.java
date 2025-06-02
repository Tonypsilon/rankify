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

    public List<Option> getOptions() {
        return List.copyOf(options);
    }
}
