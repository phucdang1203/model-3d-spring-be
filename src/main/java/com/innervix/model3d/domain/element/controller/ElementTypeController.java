package com.innervix.model3d.domain.element.controller;


import com.innervix.model3d.domain.element.model.*;
import com.innervix.model3d.domain.element.service.ElementTypeService;
import com.innervix.model3d.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ElementTypeController {

    private final ElementTypeService service;

    public ElementTypeController(ElementTypeService service) {
        this.service = service;
    }

    @GetMapping("/api/v1/element-types")
    public ApiResponse<List<ElementType>> list() {
        return ApiResponse.ok(service.listActive());
    }

    @PostMapping("/api/v1/admin/element-types")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ElementType> create(@Valid @RequestBody ElementTypeRequest request) {
        return ApiResponse.ok(service.create(request));
    }
}
