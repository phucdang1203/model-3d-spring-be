package com.innervix.model3d.domain.model.controller;


import com.innervix.model3d.domain.model.model.*;
import com.innervix.model3d.domain.model.service.ModelService;
import com.innervix.model3d.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ModelController {

    private final ModelService service;

    public ModelController(ModelService service) {
        this.service = service;
    }

    @GetMapping({"/api/v1/models", "/api/models"})
    public ApiResponse<List<ModelAsset>> list(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String format,
            @RequestParam(required = false) String elementTypeId,
            @RequestParam(required = false) String sort
    ) {
        return ApiResponse.ok(service.list(q, category, format, elementTypeId, sort));
    }

    @GetMapping({"/api/v1/models/{id}", "/api/models/{id}"})
    public ApiResponse<ModelAsset> detail(@PathVariable String id) {
        return ApiResponse.ok(service.detail(id));
    }

    @PostMapping({"/api/v1/admin/models/upload", "/api/models/upload"})
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ModelAsset> upload(@RequestParam MultipartFile file, @Valid @ModelAttribute ModelUploadRequest request) {
        return ApiResponse.ok(service.upload(file, request));
    }

    @PutMapping({"/api/v1/admin/models/{id}", "/api/models/{id}"})
    public ApiResponse<ModelAsset> update(@PathVariable String id, @RequestBody ModelUpdateRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping({"/api/v1/admin/models/{id}", "/api/models/{id}"})
    public ApiResponse<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ApiResponse.ok();
    }

    @GetMapping("/api/models/{id}/download")
    public ResponseEntity<Void> download(@PathVariable String id) {
        ModelAsset model = service.detail(id);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", model.fileUrl())
                .build();
    }
}
