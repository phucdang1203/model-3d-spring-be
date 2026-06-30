package com.innervix.model3d.domain.character.mapper;

import com.innervix.model3d.domain.character.model.GameCharacter;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface GameCharacterMapper {
    List<GameCharacter> findAll(@Param("includeInactive") boolean includeInactive);

    Optional<GameCharacter> findById(@Param("id") String id);

    int upsert(
            @Param("id") String id,
            @Param("name") String name,
            @Param("description") String description,
            @Param("modelId") String modelId,
            @Param("fileUrl") String fileUrl,
            @Param("format") String format,
            @Param("animationManifestJson") String animationManifestJson,
            @Param("baseStatsJson") String baseStatsJson,
            @Param("active") boolean active
    );

    int setActive(@Param("id") String id, @Param("active") boolean active);
}
