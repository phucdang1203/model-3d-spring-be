package com.innervix.model3d.domain.element.model;

import java.time.LocalDateTime;

public record ElementType(
        String id,
        String key,
        String name,
        String description,
        String icon,
        String color,
        Integer sortOrder,
        Boolean active,
        String schemaJson,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
