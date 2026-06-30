package com.innervix.model3d.domain.level.service;


import com.innervix.model3d.common.JsonSupport;
import com.innervix.model3d.domain.level.mapper.LevelMapper;
import com.innervix.model3d.domain.level.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Service
public class LevelService {

    private final LevelMapper mapper;
    private final JsonSupport json;

    public LevelService(LevelMapper mapper, JsonSupport json) {
        this.mapper = mapper;
        this.json = json;
    }

    @Transactional(readOnly = true)
    public List<LevelResponse> list() {
        return mapper.findAll().stream().map(level -> LevelResponse.from(level, json)).toList();
    }

    @Transactional(readOnly = true)
    public List<LevelResponse> listPublished() {
        return list().stream()
                .filter(level -> "published".equals(level.status()))
                .toList();
    }
    @Transactional(readOnly = true)
    public LevelResponse detail(String id) {
        return mapper.findById(id)
                .map(level -> LevelResponse.from(level, json))
                .orElseThrow(() -> new IllegalArgumentException("Level not found"));
    }

    @Transactional
    public LevelResponse save(LevelRequest request) {
        String id = request.id() == null || request.id().isBlank() ? UUID.randomUUID().toString() : request.id();
        String status = normalizeStatus(request.status());
        String publishedAt = "published".equals(status)
                ? valueOrNow(request.publishedAt())
                : request.publishedAt();
        String archivedAt = "archived".equals(status)
                ? valueOrNow(request.archivedAt())
                : request.archivedAt();

        mapper.upsert(
                id,
                request.name().trim(),
                request.slug() == null || request.slug().isBlank() ? slugify(request.name()) : request.slug().trim(),
                blankToNull(request.description()),
                status,
                request.mapModelUrl().trim(),
                jsonOrDefault(request.playerCharacter(), null),
                jsonOrDefault(request.playerSpawn(), List.of(0, 1.5, 0)),
                jsonOrDefault(request.robotSpawn(), List.of(0, 0, 0)),
                request.robotStory() == null ? "" : request.robotStory(),
                jsonOrDefault(request.storyGraph(), java.util.Map.of("nodes", List.of(), "edges", List.of(), "variables", List.of())),
                jsonOrDefault(request.zombieSpawns(), List.of()),
                jsonOrDefault(request.mapCharacters(), List.of()),
                jsonOrDefault(request.placedObjects(), List.of()),
                request.maxPlayers() == null ? 50 : Math.max(1, request.maxPlayers()),
                publishedAt,
                archivedAt
        );
        return mapper.findById(id)
                .map(level -> LevelResponse.from(level, json))
                .orElseThrow(() -> new IllegalStateException("Level was not saved"));
    }

    @Transactional
    public LevelResponse update(String id, LevelPatchRequest request) {
        LevelResponse current = detail(id);
        return save(new LevelRequest(
                id,
                valueOrCurrent(request.name(), current.name()),
                valueOrCurrent(request.slug(), current.slug()),
                request.description() == null ? current.description() : request.description(),
                valueOrCurrent(request.status(), current.status()),
                valueOrCurrent(request.mapModelUrl(), current.mapModelUrl()),
                request.playerCharacter() == null ? current.playerCharacter() : request.playerCharacter(),
                request.playerSpawn() == null ? current.playerSpawn() : request.playerSpawn(),
                request.robotSpawn() == null ? current.robotSpawn() : request.robotSpawn(),
                request.robotStory() == null ? current.robotStory() : request.robotStory(),
                request.storyGraph() == null ? current.storyGraph() : request.storyGraph(),
                request.zombieSpawns() == null ? current.zombieSpawns() : request.zombieSpawns(),
                request.mapCharacters() == null ? current.mapCharacters() : request.mapCharacters(),
                request.placedObjects() == null ? current.placedObjects() : request.placedObjects(),
                request.maxPlayers() == null ? current.maxPlayers() : request.maxPlayers(),
                request.publishedAt() == null ? dateToString(current.publishedAt()) : request.publishedAt(),
                request.archivedAt() == null ? dateToString(current.archivedAt()) : request.archivedAt()
        ));
    }

    @Transactional
    public LevelResponse updateStatus(String id, String status) {
        return update(id, new LevelPatchRequest(
                null, null, null, status, null, null, null, null, null,
                null, null, null, null, null, null, null
        ));
    }

    @Transactional
    public void delete(String id) {
        if (!mapper.delete(id)) {
            throw new IllegalArgumentException("Level not found");
        }
    }

    private String jsonOrDefault(Object value, Object fallback) {
        return json.write(value == null ? fallback : value);
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) {
            return "draft";
        }
        String normalized = status.toLowerCase(Locale.ROOT);
        if (!List.of("draft", "published", "archived").contains(normalized)) {
            throw new IllegalArgumentException("Invalid level status");
        }
        return normalized;
    }

    private String valueOrNow(String value) {
        return value == null || value.isBlank() ? LocalDateTime.now().toString() : value;
    }

    private String valueOrCurrent(String value, String current) {
        return value == null ? current : value;
    }

    private String dateToString(LocalDateTime value) {
        return Objects.isNull(value) ? null : value.toString();
    }

    private String slugify(String input) {
        String slug = input == null ? "" : input.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        return slug.isBlank() ? "level-" + UUID.randomUUID().toString().substring(0, 8) : slug;
    }
}
