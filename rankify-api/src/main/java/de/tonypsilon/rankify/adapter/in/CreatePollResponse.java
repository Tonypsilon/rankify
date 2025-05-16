package de.tonypsilon.rankify.adapter.in;

record CreatePollResponse(String name) {
    CreatePollResponse {
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("Name cannot be null or blank");
        }
    }
}
