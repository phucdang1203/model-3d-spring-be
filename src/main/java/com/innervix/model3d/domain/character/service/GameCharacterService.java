package com.innervix.model3d.domain.character.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innervix.model3d.common.JsonSupport;
import com.innervix.model3d.domain.character.mapper.GameCharacterMapper;
import com.innervix.model3d.domain.character.model.*;
import com.innervix.model3d.domain.level.model.LevelPatchRequest;
import com.innervix.model3d.domain.level.model.LevelResponse;
import com.innervix.model3d.domain.level.service.LevelService;
import com.innervix.model3d.domain.model.mapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GameCharacterService {

    private static final TypeReference<List<Map<String, Object>>> MAP_CHARACTER_LIST = new TypeReference<>() {
    };

    private final GameCharacterMapper mapper;
    private final LevelService levelService;
    private final ModelMapper modelMapper;
    private final JsonSupport json;
    private final ObjectMapper objectMapper;

    public GameCharacterService(
            GameCharacterMapper mapper,
            LevelService levelService,
            ModelMapper modelMapper,
            JsonSupport json,
            ObjectMapper objectMapper
    ) {
        this.mapper = mapper;
        this.levelService = levelService;
        this.modelMapper = modelMapper;
        this.json = json;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public List<GameCharacterResponse> list(boolean includeInactive) {
        return mapper.findAll(includeInactive).stream()
                .map(character -> GameCharacterResponse.from(character, json))
                .toList();
    }

    @Transactional
    public GameCharacterResponse save(GameCharacterRequest request) {
        String id = request.id() == null || request.id().isBlank()
                ? UUID.randomUUID().toString()
                : request.id();
        String modelId = blankToNull(request.modelId());
        if (modelId != null && modelMapper.findById(modelId).isEmpty()) {
            throw new IllegalArgumentException("Model asset not found");
        }
        mapper.upsert(
                id,
                request.name().trim(),
                blankToNull(request.description()),
                modelId,                request.fileUrl().trim(),
                blankToNull(request.format()),
                json.write(request.animationManifest()),
                json.write(request.baseStats()),
                request.isActive() == null || request.isActive()
        );
        return mapper.findById(id)
                .map(character -> GameCharacterResponse.from(character, json))
                .orElseThrow(() -> new IllegalStateException("Character was not saved"));
    }

    @Transactional
    public GameCharacterResponse archive(String id) {
        if (mapper.setActive(id, false) == 0) {
            throw new IllegalArgumentException("Character not found");
        }
        return mapper.findById(id)
                .map(character -> GameCharacterResponse.from(character, json))
                .orElseThrow(() -> new IllegalStateException("Character was not archived"));
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> listMapCharacters(String levelId) {
        return mapCharacters(levelService.detail(levelId).mapCharacters());
    }

    @Transactional
    public LevelResponse assignToLevel(String levelId, AssignMapCharacterRequest request) {
        GameCharacterResponse character = mapper.findById(request.characterId())
                .filter(GameCharacter::active)
                .map(row -> GameCharacterResponse.from(row, json))
                .orElseThrow(() -> new IllegalArgumentException("Map or character not found"));

        LevelResponse level = levelService.detail(levelId);
        List<Map<String, Object>> existing = new ArrayList<>(mapCharacters(level.mapCharacters()));
        existing.removeIf(entry -> request.characterId().equals(entry.get("characterId")));

        Map<String, Object> next = new LinkedHashMap<>();
        next.put("id", "map-character-" + UUID.randomUUID());
        next.put("characterId", character.id());
        next.put("modelId", character.modelId() == null || character.modelId().isBlank() ? character.id() : character.modelId());
        next.put("name", character.name());
        next.put("fileUrl", character.fileUrl());
        next.put("format", character.format());
        next.put("role", request.role() == null || request.role().isBlank() ? "playable" : request.role());
        next.put("displayLabel", request.displayLabel() == null || request.displayLabel().isBlank() ? character.name() : request.displayLabel());
        next.put("isDefault", Boolean.TRUE.equals(request.isDefault()));
        next.put("pointPrice", Math.max(0, request.pointPrice() == null ? 0 : request.pointPrice()));
        next.put("spawnPosition", request.spawnPosition());
        next.put("previewPosition", request.previewPosition());
        next.put("storyEnabled", Boolean.TRUE.equals(request.storyEnabled()));
        next.put("sortOrder", Math.max(0, request.sortOrder() == null ? existing.size() : request.sortOrder()));

        if (Boolean.TRUE.equals(next.get("isDefault"))) {
            existing.forEach(entry -> entry.put("isDefault", false));
        }
        existing.add(next);
        existing.sort(Comparator.comparingInt(entry -> {
            Object sortOrder = entry.getOrDefault("sortOrder", 0);
            return sortOrder instanceof Number number ? number.intValue() : 0;
        }));

        Object playerCharacter = level.playerCharacter();
        if (Boolean.TRUE.equals(next.get("isDefault")) && "playable".equals(next.get("role"))) {
            Map<String, Object> defaultCharacter = new LinkedHashMap<>();
            defaultCharacter.put("modelId", next.get("modelId"));
            defaultCharacter.put("name", next.get("name"));
            defaultCharacter.put("fileUrl", next.get("fileUrl"));
            defaultCharacter.put("format", next.get("format"));
            playerCharacter = defaultCharacter;
        }

        return levelService.update(levelId, new LevelPatchRequest(
                null, null, null, null, null, playerCharacter, null, null, null,
                null, null, existing, null, null, null, null
        ));
    }

    private List<Map<String, Object>> mapCharacters(JsonNode node) {
        if (node == null || !node.isArray()) {
            return new ArrayList<>();
        }
        return objectMapper.convertValue(node, MAP_CHARACTER_LIST);
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
