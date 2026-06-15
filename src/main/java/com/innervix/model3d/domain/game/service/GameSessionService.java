package com.innervix.model3d.domain.game.service;


import com.innervix.model3d.domain.game.mapper.GameSessionMapper;
import com.innervix.model3d.domain.game.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GameSessionService {

    private final GameSessionMapper mapper;

    public GameSessionService(GameSessionMapper mapper) {
        this.mapper = mapper;
    }

    @Transactional
    public GameSession start(StartGameSessionRequest request) {
        String id = UUID.randomUUID().toString();
        mapper.insert(id, request);
        GameSession session = mapper.findById(id);
        if (session == null) {
            throw new IllegalStateException("Game session was not created");
        }
        return session;
    }
}
