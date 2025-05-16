package de.tonypsilon.rankify.adapter.in;

record CreatePollCommand(String name) {
    public CreatePollCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
    }
}
