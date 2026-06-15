package com.innervix.model3d.domain.element.model;

import jakarta.validation.constraints.NotBlank;

public record ElementTypeRequest(
        @NotBlank String key,
        @NotBlank String name,
        String description,
        String icon,
        String color,
        String schemaJson
) {
}
