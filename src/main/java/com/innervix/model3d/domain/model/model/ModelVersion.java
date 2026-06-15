package com.innervix.model3d.domain.model.model;

import java.time.LocalDateTime;

public record ModelVersion(
        String id,
        String modelId,
        Integer versionNumber,
        String fileUrl,
        String changeNote,
        LocalDateTime createdAt
) {
}
