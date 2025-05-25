package de.tonypsilon.rankify.domain;

public record Option(String name) {
    public Option {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Option name cannot be null or blank");
        }
    }
}
