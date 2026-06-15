package com.innervix.model3d.domain.model.model;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ModelUploadRequest(
        @NotBlank String name,
        String description,
        List<String> tags,
        String category,
        String elementTypeId,
        String license
) {
}
