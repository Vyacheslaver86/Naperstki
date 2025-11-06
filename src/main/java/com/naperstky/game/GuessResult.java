package com.naperstky.game;


public class GuessResult {
    private final boolean correct;
    private final String message;
    private final int actualBallPosition;
    private final int guessedPosition;
    private final int playerScore;
    private final int dealerScore;
    private final boolean dealerCheated;
    private final CheatType cheatType;
    private final boolean canAccuse;
    private final boolean gameFinished;

    // НОВЫЕ ПОЛЯ ДЛЯ МЕХАНИКИ "ДОБАВИТЬ СТАКАН"
    private final boolean addCupActive;
    private final int newCupPosition;
    private final int finalBallPosition;

    // НОВЫЕ ПОЛЯ ДЛЯ МЕХАНИКИ "ПЕРЕПРЯТАТЬ"
    private final boolean reshuffleActive;
    private final int[] possiblePositions;

    public GuessResult(boolean correct, String message, int actualBallPosition,
                       int guessedPosition, int playerScore, int dealerScore,
                       boolean dealerCheated, CheatType cheatType, boolean canAccuse,
                       boolean gameFinished, boolean addCupActive, int newCupPosition,
                       int finalBallPosition, boolean reshuffleActive, int[] possiblePositions) {
        this.correct = correct;
        this.message = message;
        this.actualBallPosition = actualBallPosition;
        this.guessedPosition = guessedPosition;
        this.playerScore = playerScore;
        this.dealerScore = dealerScore;
        this.dealerCheated = dealerCheated;
        this.cheatType = cheatType;
        this.canAccuse = canAccuse;
        this.gameFinished = gameFinished;
        this.addCupActive = addCupActive;
        this.newCupPosition = newCupPosition;
        this.finalBallPosition = finalBallPosition;
        this.reshuffleActive = reshuffleActive;
        this.possiblePositions = possiblePositions;
    }

    // геттеры
    public boolean isCorrect() {
        return correct;
    }

    public String getMessage() {
        return message;
    }

    public int getActualBallPosition() {
        return actualBallPosition;
    }

    public int getGuessedPosition() {
        return guessedPosition;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getDealerScore() {
        return dealerScore;
    }

    public boolean isDealerCheated() {
        return dealerCheated;
    }

    public CheatType getCheatType() {
        return cheatType;
    }

    public boolean isCanAccuse() {
        return canAccuse;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    // НОВЫЕ ГЕТТЕРЫ ДЛЯ "ДОБАВИТЬ СТАКАН"
    public boolean isAddCupActive() {
        return addCupActive;
    }

    public int getNewCupPosition() {
        return newCupPosition;
    }

    public int getFinalBallPosition() {
        return finalBallPosition;
    }

    // НОВЫЕ ГЕТТЕРЫ ДЛЯ "ПЕРЕПРЯТАТЬ"
    public boolean isReshuffleActive() {
        return reshuffleActive;
    }

    public int[] getPossiblePositions() {
        return possiblePositions;
    }
}