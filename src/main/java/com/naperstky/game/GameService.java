package com.naperstky.game;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class GameService {
    private final Random random = new Random();

    public GameState createNewGame(String gameId) {
        GameState state = new GameState(gameId);
        state.setBallPosition(random.nextInt(3));
        return state;
    }

    public boolean isGameFinished(GameState state) {
        return state.getPlayerScore() >= 3 || state.getDealerScore() >= 3;
    }

    public void updateGameState(GameState state) {
        state.setGameActive(!isGameFinished(state));
    }

    public void incrementPlayerScore(GameState state) {
        state.setPlayerScore(state.getPlayerScore() + 1);
        updateGameState(state);
    }

    public void incrementDealerScore(GameState state) {
        state.setDealerScore(state.getDealerScore() + 1);
        updateGameState(state);
    }

    public void resetForNewRound(GameState state) {
        state.setBallPosition(random.nextInt(state.getCupCount()));
        state.setActiveCheatType(null);
        state.setOriginalBallPosition(-1);
        if (state.getCupCount() > 3) {
            state.setCupCount(3);
        }
    }
}