package com.naperstky.game;

import org.springframework.stereotype.Component;

@Component
public class SuspicionSystem {

    public enum SuspicionType {
        HIDE_IN_SLEEVE,
        SWAP_CUPS,
        ADD_CUP
    }

    public SuspicionResult checkSuspicion(GameState gameState, SuspicionType suspicionType, Integer specificCup) {
        CheatType actualCheat = gameState.getActiveCheatType();

        if (actualCheat == null) {
            return new SuspicionResult(false, "Жульничества не было! Вы ошиблись.", false, null);
        }

        boolean correctType = false;

        switch (suspicionType) {
            case HIDE_IN_SLEEVE:
                correctType = (actualCheat == CheatType.HIDE_IN_SLEEVE);
                break;
            case SWAP_CUPS:
                correctType = (actualCheat == CheatType.SWAP_CUPS);
                if (correctType && specificCup != null) {
                    correctType = (specificCup == gameState.getBallPosition());
                }
                break;
            case ADD_CUP:
                correctType = (actualCheat == CheatType.ADD_CUP);
                break;
        }

        if (correctType) {
            return new SuspicionResult(true, "Верно! Вы разоблачили жульничество!", true, actualCheat);
        } else {
            return new SuspicionResult(false,
                    "Неверно! Фактическое жульничество: " + actualCheat,
                    false, actualCheat);
        }
    }

    public boolean hasActiveCheat(GameState gameState) {
        return gameState.getActiveCheatType() != null;
    }
}