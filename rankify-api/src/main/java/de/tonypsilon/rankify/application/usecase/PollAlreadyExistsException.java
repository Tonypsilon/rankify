package de.tonypsilon.rankify.application.usecase;

import de.tonypsilon.rankify.domain.PollName;

public class PollAlreadyExistsException extends RuntimeException {
    public PollAlreadyExistsException(PollName name) {
        super("Poll with name %s already exists".formatted(name.value()));
    }
}
