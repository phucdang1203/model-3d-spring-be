package com.innervix.model3d.domain.animation.service;


import com.innervix.model3d.domain.animation.mapper.AnimationMapper;
import com.innervix.model3d.domain.animation.model.*;
import com.innervix.model3d.common.FileStorageService;
import com.innervix.model3d.common.JsonSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AnimationService {

    private final AnimationMapper mapper;
    private final FileStorageService storageService;
    private final JsonSupport json;

    public AnimationService(AnimationMapper mapper, FileStorageService storageService, JsonSupport json) {
        this.mapper = mapper;
        this.storageService = storageService;
        this.json = json;
    }

    @Transactional(readOnly = true)
    public List<AnimationAsset> list() {
        return mapper.findAll();
    }

    @Transactional
    public AnimationAsset upload(MultipartFile file, AnimationUploadRequest request) {
        String id = UUID.randomUUID().toString();
        FileStorageService.StoredFile stored = storageService.storeAnimation(id, file);
        String cleanName = stored.originalFilename().replaceFirst("\\.[^.]+$", "").replaceAll("[_-]+", " ").trim();
        String name = request.name() == null || request.name().isBlank() ? cleanName : request.name();
        String actionsJson = json.write(List.of(Map.of(
                "id", id + "-action-1",
                "name", name,
                "sourcePath", stored.originalFilename()
        )));
        AnimationAsset asset = new AnimationAsset(
                id,
                name,
                request.description(),
                request.tags() == null ? List.of() : request.tags(),
                stored.format().equals("zip") ? "pack" : "single",
                stored.originalFilename(),
                stored.format(),
                stored.fileUrl(),
                stored.size(),
                1,
                actionsJson,
                null,
                null
        );
        int inserted = mapper.insert(asset);
        if (inserted != 1) {
            throw new IllegalStateException("Animation asset was not created");
        }
        AnimationAsset created = mapper.findById(id);
        if (created == null) {
            throw new IllegalStateException("Animation asset was not found after create");
        }
        return created;
    }
}
