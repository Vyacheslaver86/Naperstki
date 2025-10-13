package com.naperstky.game;

public class CheatResult {
    private final boolean success;
    private final String message;
    private final int newBallPosition;
    private final int originalBallPosition;
    private final CheatType cheatType;

    public CheatResult(boolean success, String message) {
        this(success, message, -1, -1, null);
    }

    public CheatResult(boolean success, String message, int newBallPosition,
                       int originalBallPosition, CheatType cheatType) {
        this.success = success;
        this.message = message;
        this.newBallPosition = newBallPosition;
        this.originalBallPosition = originalBallPosition;
        this.cheatType = cheatType;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public int getNewBallPosition() { return newBallPosition; }
    public int getOriginalBallPosition() { return originalBallPosition; }
    public CheatType getCheatType() { return cheatType; }
}