package com.innervix.model3d.domain.character.model;

import java.time.LocalDateTime;

public record GameCharacter(
        String id,
        String name,
        String description,
        String modelId,
        String fileUrl,
        String format,
        String animationManifestJson,
        String baseStatsJson,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
