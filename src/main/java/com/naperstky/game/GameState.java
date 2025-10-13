package com.naperstky.game;

public class GameState {
    private int cupCount = 3;
    private int ballPosition;
    private int playerScore = 0;
    private int dealerScore = 0;
    private boolean gameActive = true;
    private CheatType activeCheatType;
    private int originalBallPosition;
    private final String gameId;

    public GameState(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public int getCupCount() {
        return cupCount;
    }

    public void setCupCount(int cupCount) {
        this.cupCount = cupCount;
    }

    public int getBallPosition() {
        return ballPosition;
    }

    public void setBallPosition(int ballPosition) {
        this.ballPosition = ballPosition;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public int getDealerScore() {
        return dealerScore;
    }

    public void setDealerScore(int dealerScore) {
        this.dealerScore = dealerScore;
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public void setGameActive(boolean gameActive) {
        this.gameActive = gameActive;
    }

    public CheatType getActiveCheatType() {
        return activeCheatType;
    }

    public void setActiveCheatType(CheatType activeCheatType) {
        this.activeCheatType = activeCheatType;
    }

    public int getOriginalBallPosition() {
        return originalBallPosition;
    }

    public void setOriginalBallPosition(int originalBallPosition) {
        this.originalBallPosition = originalBallPosition;
    }
}