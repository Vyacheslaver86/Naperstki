package com.naperstky.mvc.controller;

import com.naperstky.game.*;
import com.naperstky.mvc.service.PlayerService;
import com.naperstky.player.Player;
import com.naperstky.security.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameSessionService gameSessionService;
    private final GameService gameService;
    private final PlayerService playerService;

    // НАЧАТЬ НОВУЮ ИГРУ
    @PostMapping("/start")
    public ResponseEntity<GameState> startGame(@RequestParam String gameId) {
        GameState game = gameService.createNewGame(gameId);
        gameSessionService.createGame(gameId);
        return ResponseEntity.ok(game);
    }

    // СДЕЛАТЬ ХОД (УГАДАТЬ СТАКАН)
    @PostMapping("/guess")
    public ResponseEntity<GuessResult> makeGuess(
            @RequestParam String gameId,
            @RequestParam int guessedPosition,
            Authentication authentication) {

        GameState game = gameSessionService.getGameState(gameId);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        if (!game.isGameActive()) {
            return ResponseEntity.badRequest().build();
        }

        // Получаем игрока
        UserAccount user = (UserAccount) authentication.getPrincipal();
        Player player = playerService.getPlayerByUserWithCreation(user);

        GuessResult result = gameService.processPlayerGuess(game, guessedPosition);

        // Обновляем статистику игрока
        if (result.isCorrect() && !result.isDealerCheated()) {
            playerService.updatePlayerStats(player, true);
        } else if (!result.isCorrect() && !result.isCanAccuse() && !result.isAddCupActive() && !result.isReshuffleActive()) {
            playerService.updatePlayerStats(player, false);
        }

        return ResponseEntity.ok(result);
    }

    // ПОВТОРНОЕ УГАДЫВАНИЕ ДЛЯ МЕХАНИКИ "ДОБАВИТЬ СТАКАН"
    @PostMapping("/add-cup-guess")
    public ResponseEntity<GuessResult> makeAddCupGuess(
            @RequestParam String gameId,
            @RequestParam int guessedPosition,
            Authentication authentication) {

        GameState game = gameSessionService.getGameState(gameId);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        if (!game.isAddCupActive()) {
            return ResponseEntity.badRequest().build();
        }

        // Получаем игрока
        UserAccount user = (UserAccount) authentication.getPrincipal();
        Player player = playerService.getPlayerByUserWithCreation(user);

        GuessResult result = gameService.processAddCupGuess(game, guessedPosition);

        // Обновляем статистику игрока
        if (result.isCorrect()) {
            playerService.updatePlayerStats(player, true);
        } else {
            playerService.updatePlayerStats(player, false);
        }

        return ResponseEntity.ok(result);
    }

    // ПОВТОРНОЕ УГАДЫВАНИЕ ДЛЯ МЕХАНИКИ "ПЕРЕПРЯТАТЬ"
    @PostMapping("/reshuffle-guess")
    public ResponseEntity<GuessResult> makeReshuffleGuess(
            @RequestParam String gameId,
            @RequestParam int guessedPosition,
            Authentication authentication) {

        GameState game = gameSessionService.getGameState(gameId);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        if (!game.isReshuffleActive()) {
            return ResponseEntity.badRequest().build();
        }

        // Получаем игрока
        UserAccount user = (UserAccount) authentication.getPrincipal();
        Player player = playerService.getPlayerByUserWithCreation(user);

        GuessResult result = gameService.processReshuffleGuess(game, guessedPosition);

        // Обновляем статистику игрока
        if (result.isCorrect()) {
            playerService.updatePlayerStats(player, true);
        } else {
            playerService.updatePlayerStats(player, false);
        }

        return ResponseEntity.ok(result);
    }

    // ОБВИНИТЬ ДИЛЕРА В ЖУЛЬНИЧЕСТВЕ
    @PostMapping("/accuse")
    public ResponseEntity<AccusationResult> accuseDealer(
            @RequestParam String gameId,
            @RequestParam CheatType accusedMechanic,
            @RequestParam(required = false) Integer specificChoice,
            Authentication authentication) {

        GameState game = gameSessionService.getGameState(gameId);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        if (!game.isAccusationInProgress()) {
            return ResponseEntity.badRequest().body(
                    new AccusationResult(false, "Нельзя обвинять в этом раунде!", false, null));
        }

        // Если активна механика "Добавить стакан" или "Перепрятать" - нельзя обвинять
        if (game.isAddCupActive() || game.isReshuffleActive()) {
            return ResponseEntity.badRequest().body(
                    new AccusationResult(false, "Сначала заверши раунд с активной механикой!", false, null));
        }

        // Получаем игрока
        UserAccount user = (UserAccount) authentication.getPrincipal();
        Player player = playerService.getPlayerByUserWithCreation(user);

        AccusationResult result = gameService.processAccusation(game, accusedMechanic, specificChoice);

        // Обновляем статистику игрока
        if (result.isSuccess() && result.isCheatExposed()) {
            playerService.updatePlayerStats(player, true);
        } else if (!result.isSuccess()) {
            playerService.updatePlayerStats(player, false);
        }

        return ResponseEntity.ok(result);
    }

    // ПРИЗНАТЬ ПОРАЖЕНИЕ (НЕ ОБВИНЯТЬ)
    @PostMapping("/surrender")
    public ResponseEntity<Map<String, String>> surrender(
            @RequestParam String gameId,
            Authentication authentication) {

        GameState game = gameSessionService.getGameState(gameId);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        if (!game.isAccusationInProgress()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Нельзя сдаться в этом раунде!"));
        }

        // Если активна механика "Добавить стакан" или "Перепрятать" - нельзя сдаваться
        if (game.isAddCupActive() || game.isReshuffleActive()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Сначала заверши раунд с активной механикой!"));
        }

        // Получаем игрока
        UserAccount user = (UserAccount) authentication.getPrincipal();
        Player player = playerService.getPlayerByUserWithCreation(user);

        // Дилер получает очко
        gameService.incrementDealerScore(game);
        game.setAccusationInProgress(false);
        playerService.updatePlayerStats(player, false);

        return ResponseEntity.ok(Map.of("message", "Вы признали поражение. Дилер получает очко."));
    }

    // НАЧАТЬ НОВЫЙ РАУНД
    @PostMapping("/new-round")
    public ResponseEntity<GameState> startNewRound(@RequestParam String gameId) {
        GameState game = gameSessionService.getGameState(gameId);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        gameService.resetForNewRound(game);
        return ResponseEntity.ok(game);
    }

    // ПОЛУЧИТЬ СОСТОЯНИЕ ИГРЫ
    @GetMapping("/state/{gameId}")
    public ResponseEntity<GameState> getGameState(@PathVariable String gameId) {
        GameState game = gameSessionService.getGameState(gameId);
        return game != null ? ResponseEntity.ok(game) : ResponseEntity.notFound().build();
    }

    // ПОЛУЧИТЬ ИНФОРМАЦИЮ О ТЕКУЩЕМ РАУНДЕ
    @GetMapping("/round-info/{gameId}")
    public ResponseEntity<Map<String, Object>> getRoundInfo(@PathVariable String gameId) {
        GameState game = gameSessionService.getGameState(gameId);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> info = Map.of(
                "cupCount", game.getCupCount(),
                "addCupActive", game.isAddCupActive(),
                "newCupPosition", game.getNewCupPosition(),
                "reshuffleActive", game.isReshuffleActive(),
                "possiblePositions", game.getPossiblePositions(),
                "accusationInProgress", game.isAccusationInProgress(),
                "dealerCheated", game.isDealerCheatedThisRound(),
                "dealerMechanic", game.getDealerMechanic()
        );

        return ResponseEntity.ok(info);
    }
}