package com.innervix.model3d.domain.level.model;

import com.innervix.model3d.common.model.Vector3;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LevelRequest(
        String id,
        @NotBlank String name,
        @NotBlank String mapModelUrl,
        String playerCharacterJson,
        @NotNull Vector3 playerSpawn,
        @NotNull Vector3 robotSpawn,
        String robotStory,
        String storyGraphJson,
        String zombieSpawnsJson,
        String placedObjectsJson
) {
}
