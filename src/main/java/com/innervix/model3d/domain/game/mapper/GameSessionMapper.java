package com.innervix.model3d.domain.game.mapper;


import com.innervix.model3d.domain.game.model.*;
import org.apache.ibatis.annotations.Param;

public interface GameSessionMapper {
    int insert(@Param("id") String id, @Param("request") StartGameSessionRequest request);

    GameSession findById(@Param("id") String id);
}
