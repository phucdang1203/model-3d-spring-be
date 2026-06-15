package com.innervix.model3d.domain.model.model;


import com.innervix.model3d.common.model.Vector3;
import java.util.List;

public record ModelUpdateRequest(
        String name,
        String description,
        List<String> tags,
        String category,
        String elementTypeId,
        String license,
        Boolean hasAnimations,
        Boolean hasTextures,
        String customPropsJson,
        Vector3 position,
        Vector3 rotation,
        Vector3 scale
) {
}
