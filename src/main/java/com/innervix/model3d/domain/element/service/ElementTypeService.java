package com.innervix.model3d.domain.element.service;


import com.innervix.model3d.domain.element.mapper.ElementTypeMapper;
import com.innervix.model3d.domain.element.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ElementTypeService {

    private final ElementTypeMapper mapper;

    public ElementTypeService(ElementTypeMapper mapper) {
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<ElementType> listActive() {
        return mapper.findActive();
    }

    @Transactional
    public ElementType create(ElementTypeRequest request) {
        String id = UUID.randomUUID().toString();
        Integer sortOrder = mapper.nextSortOrder();
        mapper.insert(id, request, sortOrder);
        return mapper.findById(id).orElseThrow(() -> new IllegalStateException("Element type was not created"));
    }
}
