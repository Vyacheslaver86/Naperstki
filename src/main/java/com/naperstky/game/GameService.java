package com.naperstky.game;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class GameService {
    private final Random random = new Random();

    // ОСНОВНОЙ МЕТОД ДЛЯ ОБРАБОТКИ ВЫБОРА ИГРОКА
    public GuessResult processPlayerGuess(GameState gameState, int guessedPosition) {
        boolean isCorrect = (guessedPosition == gameState.getBallPosition());
        gameState.setPlayerGuessedCorrectly(isCorrect);

        if (isCorrect) {
            // ИГРОК УГАДАЛ - дилер может сжульничать
            return handleCorrectGuess(gameState, guessedPosition);
        } else {
            // ИГРОК НЕ УГАДАЛ - простой проигрыш
            incrementDealerScore(gameState);
            return new GuessResult(false, "Увы! Шарик был в другом стакане.",
                    gameState.getBallPosition(), guessedPosition,
                    gameState.getPlayerScore(), gameState.getDealerScore(),
                    false, null, false, isGameFinished(gameState),
                    false, -1, -1, false, null);
        }
    }

    // ОБРАБОТКА ПРАВИЛЬНОГО ОТВЕТА ИГРОКА
    private GuessResult handleCorrectGuess(GameState gameState, int guessedPosition) {
        gameState.setOriginalBallPosition(guessedPosition);

        // Дилер решает, жульничать или нет (50% шанс)
        if (random.nextBoolean()) {
            // ДИЛЕР ЖУЛЬНИЧАЕТ - выбирает механику
            return handleDealerCheat(gameState, guessedPosition);
        } else {
            // Дилер честен - игрок получает очко
            incrementPlayerScore(gameState);
            return new GuessResult(true, "Поздравляем! Вы угадали!",
                    gameState.getBallPosition(), guessedPosition,
                    gameState.getPlayerScore(), gameState.getDealerScore(),
                    false, null, false, isGameFinished(gameState),
                    false, -1, -1, false, null);
        }
    }

    // ЛОГИКА ЖУЛЬНИЧЕСТВА ДИЛЕРА
    private GuessResult handleDealerCheat(GameState gameState, int originalPosition) {
        // Дилер выбирает механику (HIDE_IN_SLEEVE, SWAP_CUPS или ADD_CUP)
        CheatType mechanic = getRandomCheatType();
        gameState.setDealerMechanic(mechanic);
        gameState.setDealerCheatedThisRound(true);

        switch (mechanic) {
            case HIDE_IN_SLEEVE:
                gameState.setAccusationInProgress(true);
                return executeHideInSleeveMechanic(gameState, originalPosition);
            case SWAP_CUPS:
                return executeReshuffleMechanic(gameState, originalPosition);
            case ADD_CUP:
                return executeAddCupMechanic(gameState, originalPosition);
            default:
                return executeReshuffleMechanic(gameState, originalPosition);
        }
    }

    // ВЫБОР СЛУЧАЙНОЙ МЕХАНИКИ ЖУЛЬНИЧЕСТВА
    private CheatType getRandomCheatType() {
        int choice = random.nextInt(3);
        switch (choice) {
            case 0: return CheatType.HIDE_IN_SLEEVE;
            case 1: return CheatType.SWAP_CUPS;
            case 2: return CheatType.ADD_CUP;
            default: return CheatType.SWAP_CUPS;
        }
    }

    // МЕХАНИКА "СПРЯТАТЬ В РУКАВ"
    private GuessResult executeHideInSleeveMechanic(GameState gameState, int originalPosition) {
        // Сохраняем выбор рукава (0-левый, 1-правый)
        gameState.setDealerChoice(random.nextInt(2));
        gameState.setBallPosition(-1); // Шарик в рукаве

        return new GuessResult(true,
                "Но под стаканом ничего нет! Кажется, дилер сжульничал...",
                -1, originalPosition,
                gameState.getPlayerScore(), gameState.getDealerScore(),
                true, CheatType.HIDE_IN_SLEEVE, true, isGameFinished(gameState),
                false, -1, -1, false, null);
    }

    // МЕХАНИКА "ПЕРЕПРЯТАТЬ" - НОВАЯ ЛОГИКА
    private GuessResult executeReshuffleMechanic(GameState gameState, int originalPosition) {
        // Определяем возможные позиции для перемещения по правилам
        int[] possiblePositions = getPossiblePositions(originalPosition);
        gameState.setPossiblePositions(possiblePositions);

        // Дилер тайно выбирает новую позицию из возможных
        int newPosition = possiblePositions[random.nextInt(possiblePositions.length)];
        gameState.setDealerChoice(newPosition);
        gameState.setBallPosition(newPosition);
        gameState.setReshuffleActive(true);

        String message = "Дилер быстро перемешал все стаканы! Угадывай заново!";

        return new GuessResult(true,
                message,
                newPosition, originalPosition,
                gameState.getPlayerScore(), gameState.getDealerScore(),
                true, CheatType.SWAP_CUPS, false, isGameFinished(gameState),
                false, -1, -1, true, possiblePositions);
    }

    // ОПРЕДЕЛЕНИЕ ВОЗМОЖНЫХ ПОЗИЦИЙ ДЛЯ ПЕРЕМЕЩЕНИЯ
    private int[] getPossiblePositions(int originalPosition) {
        switch (originalPosition) {
            case 0: // Крайний левый - можно переместить только в 0 или 2
                return new int[]{0, 2};
            case 1: // Средний - можно переместить в 0, 1 или 2
                return new int[]{0, 1, 2};
            case 2: // Крайний правый - можно переместить только в 2 или 0
                return new int[]{2, 0};
            default:
                return new int[]{0, 1, 2};
        }
    }

    // МЕХАНИКА "ДОБАВИТЬ СТАКАН"
    private GuessResult executeAddCupMechanic(GameState gameState, int originalPosition) {
        // Увеличиваем количество стаканов до 4
        gameState.setCupCount(4);

        // Определяем позицию для нового стакана (слева или справа от правильного)
        int newCupPosition = determineNewCupPosition(originalPosition, gameState.getCupCount());
        gameState.setNewCupPosition(newCupPosition);

        // Тайное решение: оставить шарик в исходном или переместить в новый
        boolean moveToNewCup = random.nextBoolean();
        int finalBallPosition;

        if (moveToNewCup) {
            // Перемещаем шарик в новый стакан
            finalBallPosition = newCupPosition;
        } else {
            // Оставляем шарик в исходном стакане (но с учетом новой нумерации)
            finalBallPosition = adjustOriginalPosition(originalPosition, newCupPosition);
        }

        gameState.setDealerChoice(moveToNewCup ? 1 : 0); // 1 - переместил, 0 - оставил
        gameState.setBallPosition(finalBallPosition);
        gameState.setAddCupActive(true);

        String message = "Дилер добавил новый стакан! Теперь угадывай из " + gameState.getCupCount() + " стаканов.";

        return new GuessResult(true,
                message,
                finalBallPosition, originalPosition,
                gameState.getPlayerScore(), gameState.getDealerScore(),
                true, CheatType.ADD_CUP, false, isGameFinished(gameState),
                true, newCupPosition, finalBallPosition, false, null);
    }

    // ОПРЕДЕЛЕНИЕ ПОЗИЦИИ НОВОГО СТАКАНА ОТНОСИТЕЛЬНО ПРАВИЛЬНОГО
    private int determineNewCupPosition(int originalPosition, int cupCount) {
        if (originalPosition == 0) {
            // Правильный стакан №0 - можно добавить только СПРАВА
            return 1;
        } else if (originalPosition == cupCount - 2) { // -2 потому что пока еще 3 стакана
            // Правильный стакан №2 - можно добавить только СЛЕВА
            return originalPosition;
        } else {
            // Правильный стакан №1 - можно добавить СЛЕВА или СПРАВА
            return random.nextBoolean() ? originalPosition : originalPosition + 1;
        }
    }

    // КОРРЕКТИРОВКА ПОЗИЦИИ ИСХОДНОГО СТАКАНА ПРИ ДОБАВЛЕНИИ НОВОГО
    private int adjustOriginalPosition(int originalPosition, int newCupPosition) {
        // Если новый стакан добавлен слева от исходного, то исходный сдвигается вправо
        if (newCupPosition <= originalPosition) {
            return originalPosition + 1;
        } else {
            // Если новый стакан добавлен справа, то исходный остается на месте
            return originalPosition;
        }
    }

    // МЕТОД ДЛЯ ОБРАБОТКИ ПОВТОРНОГО УГАДЫВАНИЯ ПРИ "ДОБАВИТЬ СТАКАН"
    public GuessResult processAddCupGuess(GameState gameState, int guessedPosition) {
        if (!gameState.isAddCupActive()) {
            return new GuessResult(false, "Механика 'Добавить стакан' не активна!",
                    -1, guessedPosition,
                    gameState.getPlayerScore(), gameState.getDealerScore(),
                    false, null, false, isGameFinished(gameState),
                    false, -1, -1, false, null);
        }

        boolean isCorrect = (guessedPosition == gameState.getBallPosition());

        if (isCorrect) {
            incrementPlayerScore(gameState);
            gameState.setAddCupActive(false);
            gameState.setAccusationInProgress(false);
            return new GuessResult(true, "Поздравляем! Вы угадали с новым стаканом!",
                    gameState.getBallPosition(), guessedPosition,
                    gameState.getPlayerScore(), gameState.getDealerScore(),
                    true, CheatType.ADD_CUP, false, isGameFinished(gameState),
                    false, -1, -1, false, null);
        } else {
            incrementDealerScore(gameState);
            gameState.setAddCupActive(false);
            gameState.setAccusationInProgress(false);
            return new GuessResult(false, "Увы! Шарик был в другом стакане.",
                    gameState.getBallPosition(), guessedPosition,
                    gameState.getPlayerScore(), gameState.getDealerScore(),
                    true, CheatType.ADD_CUP, false, isGameFinished(gameState),
                    false, -1, -1, false, null);
        }
    }

    // МЕТОД ДЛЯ ОБРАБОТКИ ПОВТОРНОГО УГАДЫВАНИЯ ПРИ "ПЕРЕПРЯТАТЬ"
    public GuessResult processReshuffleGuess(GameState gameState, int guessedPosition) {
        if (!gameState.isReshuffleActive()) {
            return new GuessResult(false, "Механика 'Перепрятать' не активна!",
                    -1, guessedPosition,
                    gameState.getPlayerScore(), gameState.getDealerScore(),
                    false, null, false, isGameFinished(gameState),
                    false, -1, -1, false, null);
        }

        boolean isCorrect = (guessedPosition == gameState.getBallPosition());

        if (isCorrect) {
            incrementPlayerScore(gameState);
            gameState.setReshuffleActive(false);
            return new GuessResult(true, "Поздравляем! Вы угадали после перемешивания!",
                    gameState.getBallPosition(), guessedPosition,
                    gameState.getPlayerScore(), gameState.getDealerScore(),
                    true, CheatType.SWAP_CUPS, false, isGameFinished(gameState),
                    false, -1, -1, false, null);
        } else {
            incrementDealerScore(gameState);
            gameState.setReshuffleActive(false);
            return new GuessResult(false, "Увы! Шарик был в другом стакане.",
                    gameState.getBallPosition(), guessedPosition,
                    gameState.getPlayerScore(), gameState.getDealerScore(),
                    true, CheatType.SWAP_CUPS, false, isGameFinished(gameState),
                    false, -1, -1, false, null);
        }
    }

    // МЕТОД ДЛЯ ОБРАБОТКИ ОБВИНЕНИЯ
    public AccusationResult processAccusation(GameState gameState, CheatType accusedMechanic, Integer specificChoice) {
        if (!gameState.isDealerCheatedThisRound()) {
            return new AccusationResult(false, "Дилер не жульничал в этом раунде!", false, null);
        }

        // Если активна механика "Добавить стакан" или "Перепрятать" - нельзя обвинять
        if (gameState.isAddCupActive() || gameState.isReshuffleActive()) {
            return new AccusationResult(false, "Сначала заверши раунд с активной механикой!", false, null);
        }

        CheatType actualMechanic = gameState.getDealerMechanic();
        boolean correctType = (accusedMechanic == actualMechanic);

        if (correctType) {
            // Игрок угадал тип жульничества
            return handleCorrectAccusation(gameState, actualMechanic, specificChoice);
        } else {
            // Игрок не угадал тип
            incrementDealerScore(gameState);
            gameState.setAccusationInProgress(false);
            return new AccusationResult(false,
                    "Не угадал! Дилер использовал: " + getMechanicDescription(actualMechanic),
                    false, actualMechanic);
        }
    }

    // ОБРАБОТКА ПРАВИЛЬНОГО ОБВИНЕНИЯ
    private AccusationResult handleCorrectAccusation(GameState gameState, CheatType actualMechanic, Integer specificChoice) {
        switch (actualMechanic) {
            case HIDE_IN_SLEEVE:
                return handleSleeveAccusation(gameState, specificChoice);
            case SWAP_CUPS:
                return handleSwapAccusation(gameState, specificChoice);
            case ADD_CUP:
                // Для ADD_CUP обвинение всегда успешно (игрок уже угадал тип)
                incrementPlayerScore(gameState);
                gameState.setAccusationInProgress(false);
                return new AccusationResult(true,
                        "Верно! Дилер добавил стакан чтобы запутать тебя!",
                        true, CheatType.ADD_CUP);
            default:
                incrementDealerScore(gameState);
                gameState.setAccusationInProgress(false);
                return new AccusationResult(false, "Неизвестный тип жульничества", false, actualMechanic);
        }
    }

    // ПРОВЕРКА ОБВИНЕНИЯ ДЛЯ РУКАВА
    private AccusationResult handleSleeveAccusation(GameState gameState, Integer chosenSleeve) {
        if (chosenSleeve == null) {
            incrementDealerScore(gameState);
            gameState.setAccusationInProgress(false);
            return new AccusationResult(false, "Нужно выбрать рукав!", false, CheatType.HIDE_IN_SLEEVE);
        }

        boolean correctSleeve = (chosenSleeve == gameState.getDealerChoice());

        if (correctSleeve) {
            incrementPlayerScore(gameState);
            gameState.setAccusationInProgress(false);
            return new AccusationResult(true,
                    "Верно! Шарик в " + (chosenSleeve == 0 ? "левом" : "правом") + " рукаве!",
                    true, CheatType.HIDE_IN_SLEEVE);
        } else {
            incrementDealerScore(gameState);
            gameState.setAccusationInProgress(false);
            return new AccusationResult(false,
                    "Не угадал! Шарик был в " + (gameState.getDealerChoice() == 0 ? "левом" : "правом") + " рукаве!",
                    false, CheatType.HIDE_IN_SLEEVE);
        }
    }

    // ПРОВЕРКА ОБВИНЕНИЯ ДЛЯ ПЕРЕКЛАДЫВАНИЯ
    private AccusationResult handleSwapAccusation(GameState gameState, Integer chosenCup) {
        if (chosenCup == null) {
            incrementDealerScore(gameState);
            gameState.setAccusationInProgress(false);
            return new AccusationResult(false, "Нужно выбрать стакан!", false, CheatType.SWAP_CUPS);
        }

        boolean correctCup = (chosenCup == gameState.getDealerChoice());

        if (correctCup) {
            incrementPlayerScore(gameState);
            gameState.setAccusationInProgress(false);
            return new AccusationResult(true,
                    "Верно! Шарик в стакане №" + (chosenCup + 1) + "!",
                    true, CheatType.SWAP_CUPS);
        } else {
            incrementDealerScore(gameState);
            gameState.setAccusationInProgress(false);
            return new AccusationResult(false,
                    "Не угадал! Шарик был в стакане №" + (gameState.getDealerChoice() + 1) + "!",
                    false, CheatType.SWAP_CUPS);
        }
    }

    // ВСПОМОГАТЕЛЬНЫЙ МЕТОД ДЛЯ ОПИСАНИЯ МЕХАНИКИ
    private String getMechanicDescription(CheatType mechanic) {
        switch (mechanic) {
            case HIDE_IN_SLEEVE:
                return "Спрятал в рукав";
            case SWAP_CUPS:
                return "Перепрятал в стакан";
            case ADD_CUP:
                return "Добавил стакан";
            default:
                return "Неизвестная механика";
        }
    }

    // СУЩЕСТВУЮЩИЕ МЕТОДЫ
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
        state.setDealerMechanic(null);
        state.setPlayerGuessedCorrectly(false);
        state.setDealerCheatedThisRound(false);
        state.setAccusationInProgress(false);
        state.setAddCupActive(false);
        state.setNewCupPosition(-1);
        state.setReshuffleActive(false);
        state.setPossiblePositions(null);

        // Всегда возвращаем к 3 стаканам в новом раунде
        if (state.getCupCount() > 3) {
            state.setCupCount(3);
        }
    }
}
