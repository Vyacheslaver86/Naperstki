package com.naperstky.game;

public class SuspicionResult {
    private final boolean success;
    private final String message;
    private final boolean cheatExposed;
    private final CheatType actualCheatType;

    public SuspicionResult(boolean success, String message, boolean cheatExposed, CheatType actualCheatType) {
        this.success = success;
        this.message = message;
        this.cheatExposed = cheatExposed;
        this.actualCheatType = actualCheatType;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public boolean isCheatExposed() { return cheatExposed; }
    public CheatType getActualCheatType() { return actualCheatType; }
}