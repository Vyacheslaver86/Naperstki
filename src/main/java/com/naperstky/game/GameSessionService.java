package com.naperstky.game;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameSessionService {
    private final Map<String, GameState> activeGames = new ConcurrentHashMap<>();

    public GameState createGame(String gameId) {
        GameState gameState = new GameState(gameId);
        activeGames.put(gameId, gameState);
        return gameState;
    }

    public GameState getGameState(String gameId) {
        return activeGames.get(gameId);
    }

    public void endGame(String gameId) {
        activeGames.remove(gameId);
    }

    public boolean gameExists(String gameId) {
        return activeGames.containsKey(gameId);
    }
}