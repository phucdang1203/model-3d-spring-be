package com.innervix.model3d.domain.character.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.innervix.model3d.common.JsonSupport;

import java.time.LocalDateTime;

public record GameCharacterResponse(
        String id,
        String name,
        String description,
        String modelId,
        String fileUrl,
        String format,
        JsonNode animationManifest,
        JsonNode baseStats,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static GameCharacterResponse from(GameCharacter character, JsonSupport json) {
        return new GameCharacterResponse(
                character.id(),
                character.name(),
                character.description(),
                character.modelId(),
                character.fileUrl(),
                character.format(),
                json.readTree(character.animationManifestJson(), "null"),
                json.readTree(character.baseStatsJson(), "null"),
                character.active(),
                character.createdAt(),
                character.updatedAt()
        );
    }
}
