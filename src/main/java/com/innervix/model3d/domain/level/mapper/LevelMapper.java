package com.innervix.model3d.domain.level.mapper;


import com.innervix.model3d.domain.level.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface LevelMapper {
    List<Level> findAll();

    Optional<Level> findById(@Param("id") String id);

    int upsert(@Param("id") String id, @Param("request") LevelRequest request);

    boolean delete(@Param("id") String id);
}
