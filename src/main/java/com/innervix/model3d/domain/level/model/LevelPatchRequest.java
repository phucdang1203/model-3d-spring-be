package com.innervix.model3d.domain.level.model;

public record LevelPatchRequest(
        String name,
        String slug,
        String description,
        String status,
        String mapModelUrl,
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
