package com.innervix.model3d.domain.model.service;


import com.innervix.model3d.common.model.Vector3;
import com.innervix.model3d.domain.model.mapper.ModelMapper;
import com.innervix.model3d.domain.model.model.*;
import com.innervix.model3d.common.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static com.innervix.model3d.common.FileStorageService.PUBLIC_UPLOAD_PATH;

@Service
public class ModelService {

    private final ModelMapper mapper;
    private final FileStorageService storageService;

    public ModelService(ModelMapper mapper, FileStorageService storageService) {
        this.mapper = mapper;
        this.storageService = storageService;
    }

    @Transactional(readOnly = true)
    public List<ModelAsset> list(String q, String category, String format, String elementTypeId, String sort) {
        return mapper.findAll(q, category, format, elementTypeId, sort).stream()
                .map(this::withPublicUploadUrl)
                .toList();
    }

    @Transactional
    public ModelAsset detail(String id) {
        mapper.incrementView(id);
        return mapper.findById(id)
                .map(this::withPublicUploadUrl)
                .orElseThrow(() -> new IllegalArgumentException("Model not found"));
    }

    @Transactional
    public ModelAsset upload(MultipartFile file, ModelUploadRequest request) {
        String id = UUID.randomUUID().toString();
        String category = request.category() == null || request.category().isBlank() ? "other" : request.category();
        FileStorageService.StoredFile stored = storageService.storeModel(category, id, file);
        ModelAsset model = new ModelAsset(
                id,
                request.name(),
                request.description(),
                request.tags() == null ? List.of() : request.tags(),
                category,
                request.elementTypeId(),
                request.license() == null || request.license().isBlank() ? "CC0" : request.license(),
                stored.originalFilename(),
                stored.format(),
                stored.fileUrl(),
                null,
                stored.size(),
                null,
                null,
                null,
                false,
                false,
                null,
                null,
                Vector3.zero(),
                Vector3.zero(),
                Vector3.one(),
                0,
                0,
                null,
                null
        );
        int inserted = mapper.insert(model);
        if (inserted != 1) {
            throw new IllegalStateException("Model was not created");
        }
        return mapper.findById(id).map(this::withPublicUploadUrl).orElseThrow();
    }

    @Transactional
    public ModelAsset update(String id, ModelUpdateRequest request) {
        int updated = mapper.update(id, request);
        if (updated == 0) {
            throw new IllegalArgumentException("Model not found");
        }
        return mapper.findById(id)
                .map(this::withPublicUploadUrl)
                .orElseThrow(() -> new IllegalArgumentException("Model not found"));
    }

    @Transactional
    public void delete(String id) {
        if (!mapper.delete(id)) {
            throw new IllegalArgumentException("Model not found");
        }
    }

    private ModelAsset withPublicUploadUrl(ModelAsset model) {
        return new ModelAsset(
                model.id(),
                model.name(),
                model.description(),
                model.tags(),
                model.category(),
                model.elementTypeId(),
                model.license(),
                model.originalFilename(),
                model.format(),
                normalizePublicUploadUrl(model.fileUrl()),
                normalizePublicUploadUrl(model.thumbnailUrl()),
                model.fileSize(),
                model.polygonCount(),
                model.vertexCount(),
                model.materialCount(),
                model.hasAnimations(),
                model.hasTextures(),
                model.customPropsJson(),
                model.boundingBoxJson(),
                model.position(),
                model.rotation(),
                model.scale(),
                model.downloadCount(),
                model.viewCount(),
                model.createdAt(),
                model.updatedAt()
        );
    }

    private String normalizePublicUploadUrl(String url) {
        if (url == null || url.isBlank()) {
            return url;
        }
        if (url.startsWith(PUBLIC_UPLOAD_PATH + "/")) {
            return url;
        }
        if (url.startsWith("/uploads/")) {
            return PUBLIC_UPLOAD_PATH + url.substring("/uploads".length());
        }
        if (url.startsWith("/uploads-files/")) {
            return PUBLIC_UPLOAD_PATH + url.substring("/uploads-files".length());
        }
        return url;
    }
}
