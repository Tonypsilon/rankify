package de.tonypsilon.rankify.adapter.in.poll;

public record Option(String name) {
    public Option {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Option name cannot be null or blank");
        }
    }
}
