package com.innervix.model3d.domain.level.model;

import jakarta.validation.constraints.NotBlank;

public record LevelRequest(
        String id,
        @NotBlank String name,
        String slug,
        String description,
        String status,
        @NotBlank String mapModelUrl,
        Object playerCharacter,
        Object playerSpawn,
        Object robotSpawn,
        String robotStory,
        Object storyGraph,
        Object zombieSpawns,
        Object mapCharacters,
        Object placedObjects,
        Integer maxPlayers,
        String publishedAt,
        String archivedAt
) {
}
