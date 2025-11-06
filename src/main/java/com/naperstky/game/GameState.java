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

    // ПОЛЯ ДЛЯ МЕХАНИКИ ДИЛЕРА
    private CheatType dealerMechanic;
    private boolean playerGuessedCorrectly = false;
    private int dealerChoice; // для рукава (0-левый, 1-правый) или нового стакана
    private boolean dealerCheatedThisRound = false;
    private boolean accusationInProgress = false;

    // НОВЫЕ ПОЛЯ ДЛЯ МЕХАНИКИ "ДОБАВИТЬ СТАКАН"
    private boolean addCupActive = false;
    private int newCupPosition = -1;

    // НОВЫЕ ПОЛЯ ДЛЯ МЕХАНИКИ "ПЕРЕПРЯТАТЬ"
    private boolean reshuffleActive = false;
    private int[] possiblePositions; // возможные позиции для угадывания

    public GameState(String gameId) {
        this.gameId = gameId;
    }

    // СУЩЕСТВУЮЩИЕ ГЕТТЕРЫ И СЕТТЕРЫ
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

    // ГЕТТЕРЫ И СЕТТЕРЫ ДЛЯ МЕХАНИКИ ДИЛЕРА
    public CheatType getDealerMechanic() {
        return dealerMechanic;
    }

    public void setDealerMechanic(CheatType dealerMechanic) {
        this.dealerMechanic = dealerMechanic;
    }

    public boolean isPlayerGuessedCorrectly() {
        return playerGuessedCorrectly;
    }

    public void setPlayerGuessedCorrectly(boolean playerGuessedCorrectly) {
        this.playerGuessedCorrectly = playerGuessedCorrectly;
    }

    public int getDealerChoice() {
        return dealerChoice;
    }

    public void setDealerChoice(int dealerChoice) {
        this.dealerChoice = dealerChoice;
    }

    public boolean isDealerCheatedThisRound() {
        return dealerCheatedThisRound;
    }

    public void setDealerCheatedThisRound(boolean dealerCheatedThisRound) {
        this.dealerCheatedThisRound = dealerCheatedThisRound;
    }

    public boolean isAccusationInProgress() {
        return accusationInProgress;
    }

    public void setAccusationInProgress(boolean accusationInProgress) {
        this.accusationInProgress = accusationInProgress;
    }

    // НОВЫЕ ГЕТТЕРЫ И СЕТТЕРЫ ДЛЯ "ДОБАВИТЬ СТАКАН"
    public boolean isAddCupActive() {
        return addCupActive;
    }

    public void setAddCupActive(boolean addCupActive) {
        this.addCupActive = addCupActive;
    }

    public int getNewCupPosition() {
        return newCupPosition;
    }

    public void setNewCupPosition(int newCupPosition) {
        this.newCupPosition = newCupPosition;
    }

    // НОВЫЕ ГЕТТЕРЫ И СЕТТЕРЫ ДЛЯ "ПЕРЕПРЯТАТЬ"
    public boolean isReshuffleActive() {
        return reshuffleActive;
    }

    public void setReshuffleActive(boolean reshuffleActive) {
        this.reshuffleActive = reshuffleActive;
    }

    public int[] getPossiblePositions() {
        return possiblePositions;
    }

    public void setPossiblePositions(int[] possiblePositions) {
        this.possiblePositions = possiblePositions;
    }
}