package com.innervix.model3d.domain.level.service;


import com.innervix.model3d.domain.level.mapper.LevelMapper;
import com.innervix.model3d.domain.level.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class LevelService {

    private final LevelMapper mapper;

    public LevelService(LevelMapper mapper) {
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<Level> list() {
        return mapper.findAll();
    }

    @Transactional(readOnly = true)
    public Level detail(String id) {
        return mapper.findById(id).orElseThrow(() -> new IllegalArgumentException("Level not found"));
    }

    @Transactional
    public Level save(LevelRequest request) {
        String id = request.id() == null || request.id().isBlank() ? UUID.randomUUID().toString() : request.id();
        mapper.upsert(id, request);
        return mapper.findById(id).orElseThrow(() -> new IllegalStateException("Level was not saved"));
    }

    @Transactional
    public void delete(String id) {
        if (!mapper.delete(id)) {
            throw new IllegalArgumentException("Level not found");
        }
    }
}
