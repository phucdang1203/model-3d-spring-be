package com.innervix.model3d.domain.model.model;


import com.innervix.model3d.common.model.Vector3;
import java.time.LocalDateTime;
import java.util.List;

public record ModelAsset(
        String id,
        String name,
        String description,
        List<String> tags,
        String category,
        String elementTypeId,
        String license,
        String originalFilename,
        String format,
        String fileUrl,
        String thumbnailUrl,
        Long fileSize,
        Integer polygonCount,
        Integer vertexCount,
        Integer materialCount,
        Boolean hasAnimations,
        Boolean hasTextures,
        String customPropsJson,
        String boundingBoxJson,
        Vector3 position,
        Vector3 rotation,
        Vector3 scale,
        Integer downloadCount,
        Integer viewCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
