package com.innervix.model3d.domain.animation.model;

import java.time.LocalDateTime;
import java.util.List;

public record AnimationAsset(
        String id,
        String name,
        String description,
        List<String> tags,
        String sourceKind,
        String originalFilename,
        String format,
        String fileUrl,
        Long fileSize,
        Integer actionCount,
        String actionsJson,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
