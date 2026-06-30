package com.innervix.model3d.domain.character.model;

import jakarta.validation.constraints.NotBlank;

public record AssignMapCharacterRequest(
        @NotBlank String characterId,
        String role,
        String displayLabel,
        Boolean isDefault,
        Integer pointPrice,
        Object spawnPosition,
        Object previewPosition,
        Boolean storyEnabled,
        Integer sortOrder
) {
}
