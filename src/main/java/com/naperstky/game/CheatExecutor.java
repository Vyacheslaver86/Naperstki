package com.naperstky.game;

import com.naperstky.player.Player;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class CheatExecutor {
    private final Random random = new Random();

    public CheatResult executeHideInSleeve(Player player, GameState gameState, int playerChoice) {
        if (!player.useCheatCoin(CheatType.HIDE_IN_SLEEVE)) {
            return new CheatResult(false, "Нет монетки для 'Спрятать в рукав'");
        }

        gameState.setOriginalBallPosition(playerChoice);
        gameState.setBallPosition(-1);
        gameState.setActiveCheatType(CheatType.HIDE_IN_SLEEVE);
        player.recordSuccessfulCheat();

        return new CheatResult(
                true,
                "Шарик таинственно исчез... Все стаканы пустые!",
                -1,
                playerChoice,
                CheatType.HIDE_IN_SLEEVE
        );
    }

    public CheatResult executeAddCup(Player player, GameState gameState, int playerChoice) {
        if (!player.useCheatCoin(CheatType.ADD_CUP)) {
            return new CheatResult(false, "Нет монетки для 'Добавить стакан'");
        }

        gameState.setCupCount(4);
        gameState.setOriginalBallPosition(playerChoice);

        boolean moveToNewCup = random.nextBoolean();
        int newPosition = moveToNewCup ? 3 : gameState.getBallPosition();
        gameState.setBallPosition(newPosition);
        gameState.setActiveCheatType(CheatType.ADD_CUP);
        player.recordSuccessfulCheat();

        String message = moveToNewCup
                ? "Появился 4-й стакан! Шарик перемещен."
                : "Появился 4-й стакан! Шарик остался на месте.";

        return new CheatResult(true, message, newPosition, playerChoice, CheatType.ADD_CUP);
    }

    public CheatResult executeSwapCups(Player player, GameState gameState, int playerChoice) {
        if (!player.useCheatCoin(CheatType.SWAP_CUPS)) {
            return new CheatResult(false, "Нет монетки для 'Переложить в другой стакан'");
        }

        gameState.setOriginalBallPosition(playerChoice);

        int newPosition;
        do {
            newPosition = random.nextInt(3);
        } while (newPosition == playerChoice);

        gameState.setBallPosition(newPosition);
        gameState.setActiveCheatType(CheatType.SWAP_CUPS);
        player.recordSuccessfulCheat();

        return new CheatResult(
                true,
                "Стаканы перемешаны! Шарик теперь в другом месте.",
                newPosition,
                playerChoice,
                CheatType.SWAP_CUPS
        );
    }
}