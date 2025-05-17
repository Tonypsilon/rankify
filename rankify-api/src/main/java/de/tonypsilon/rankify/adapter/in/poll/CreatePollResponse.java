package de.tonypsilon.rankify.adapter.in.poll;

record CreatePollResponse(String name) {
    CreatePollResponse {
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("Name cannot be null or blank");
        }
    }
}
