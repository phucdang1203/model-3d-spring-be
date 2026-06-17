package com.innervix.model3d.domain.level.mapper;


import com.innervix.model3d.domain.level.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface LevelMapper {
    List<Level> findAll();

    Optional<Level> findById(@Param("id") String id);

    int upsert(
            @Param("id") String id,
            @Param("name") String name,
            @Param("slug") String slug,
            @Param("description") String description,
            @Param("status") String status,
            @Param("mapModelUrl") String mapModelUrl,
            @Param("playerCharacterJson") String playerCharacterJson,
            @Param("playerSpawnJson") String playerSpawnJson,
            @Param("robotSpawnJson") String robotSpawnJson,
            @Param("robotStory") String robotStory,
            @Param("storyGraphJson") String storyGraphJson,
            @Param("zombieSpawnsJson") String zombieSpawnsJson,
            @Param("mapCharactersJson") String mapCharactersJson,
            @Param("placedObjectsJson") String placedObjectsJson,
            @Param("maxPlayers") Integer maxPlayers,
            @Param("publishedAt") String publishedAt,
            @Param("archivedAt") String archivedAt
    );

    boolean delete(@Param("id") String id);
}
