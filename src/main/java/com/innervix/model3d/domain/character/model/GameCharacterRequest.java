package com.innervix.model3d.domain.character.model;

import jakarta.validation.constraints.NotBlank;

public record GameCharacterRequest(
        String id,
        @NotBlank String name,
        String description,
        String modelId,
        @NotBlank String fileUrl,
        String format,
        Object animationManifest,
        Object baseStats,
        Boolean isActive
) {
}
