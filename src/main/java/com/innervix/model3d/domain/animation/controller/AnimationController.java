package com.innervix.model3d.domain.animation.controller;


import com.innervix.model3d.domain.animation.model.*;
import com.innervix.model3d.domain.animation.service.AnimationService;
import com.innervix.model3d.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class AnimationController {

    private final AnimationService service;

    public AnimationController(AnimationService service) {
        this.service = service;
    }

    @GetMapping({"/api/v1/admin/animations", "/api/animations"})
    public ApiResponse<List<AnimationAsset>> list() {
        return ApiResponse.ok(service.list());
    }

    @PostMapping({"/api/v1/admin/animations/upload", "/api/animations/upload"})
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AnimationAsset> upload(@RequestParam MultipartFile file, @ModelAttribute AnimationUploadRequest request) {
        return ApiResponse.ok(service.upload(file, request));
    }
}
