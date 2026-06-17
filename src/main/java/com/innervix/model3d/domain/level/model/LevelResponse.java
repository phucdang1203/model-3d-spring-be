package com.innervix.model3d.domain.level.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.innervix.model3d.common.JsonSupport;

import java.time.LocalDateTime;

public record LevelResponse(
        String id,
        String name,
        String slug,
        String description,
        String status,
        String mapModelUrl,
        JsonNode playerCharacter,
        JsonNode playerSpawn,
        JsonNode robotSpawn,
        String robotStory,
        JsonNode storyGraph,
        JsonNode zombieSpawns,
        JsonNode mapCharacters,
        JsonNode placedObjects,
        Integer maxPlayers,
        LocalDateTime publishedAt,
        LocalDateTime archivedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static LevelResponse from(Level level, JsonSupport json) {
        return new LevelResponse(
                level.id(),
                level.name(),
                level.slug(),
                level.description(),
                level.status(),
                level.mapModelUrl(),
                json.readTree(level.playerCharacterJson(), "null"),
                json.readTree(level.playerSpawnJson(), "[0,1.5,0]"),
                json.readTree(level.robotSpawnJson(), "[0,0,0]"),
                level.robotStory(),
                json.readTree(level.storyGraphJson(), "{\"nodes\":[],\"edges\":[],\"variables\":[]}"),
                json.readTree(level.zombieSpawnsJson(), "[]"),
                json.readTree(level.mapCharactersJson(), "[]"),
                json.readTree(level.placedObjectsJson(), "[]"),
                level.maxPlayers(),
                level.publishedAt(),
                level.archivedAt(),
                level.createdAt(),
                level.updatedAt()
        );
    }
}
