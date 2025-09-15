package com.naperstky.player;

import com.naperstky.game.CheatCoin;
import com.naperstky.game.CheatType;
import com.naperstky.security.UserAccount;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Getter
@Setter
@ToString(exclude = "userAccount")
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account_id", referencedColumnName = "id")
    private UserAccount userAccount;


    @Column(name = "total_games_played")
    private int gamesPlayed = 0;

    @Column(name = "wins")
    private int wins = 0;

    @Column(name = "current_streak")
    private int currentStreak = 0;

    @Column(name = "loses")
    private int loses = 0;
    @Column(name = "coins")
    private int coins = 0;


    // Статистика жульничества
    @Column(name = "cheat_attempts")
    private int cheatAttempts = 0;

    @Column(name = "successful_cheats")
    private int successfulCheats = 0;

    @Column(name = "caught_cheats")
    private int caughtCheats = 0;

    // Коллекция монеток для жульничества
    @ElementCollection
    @CollectionTable(
            name = "player_cheat_coins",
            joinColumns = @JoinColumn(name = "player_id")
    )
    private List<CheatCoin> cheatCoinsList = new ArrayList<>();

    // Инициализация монеток при создании игрока
    @PostPersist
    public void init() {
        if (this.cheatCoinsList.isEmpty()) {
            this.cheatCoinsList.add(new CheatCoin(CheatType.HIDE_IN_SLEEVE));
            this.cheatCoinsList.add(new CheatCoin(CheatType.SWAP_CUPS));
        }
    }

    // Методы для работы с жульничеством
    public boolean useCheatCoin(CheatType type) {
        Optional<CheatCoin> coin = cheatCoinsList.stream()
                .filter(c -> c.getType() == type && !c.isUsed())
                .findFirst();

        if (coin.isPresent()) {
            coin.get().setUsed(true);
            this.cheatAttempts++;
            return true;
        }
        return false;
    }

    public void addCheatCoin(CheatType type) {
        this.cheatCoinsList.add(new CheatCoin(type));
    }

    public void recordSuccessfulCheat() {
        this.successfulCheats++;
    }

    public void recordCaughtCheating() {
        this.caughtCheats++;
    }

    // Расчетные показатели
    public double getCheatSuccessRate() {
        return cheatAttempts > 0 ? (double) successfulCheats / cheatAttempts * 100 : 0;
    }

    public double getCheatDetectionRate() {
        return cheatAttempts > 0 ? (double) caughtCheats / cheatAttempts * 100 : 0;
    }

    public void addCoins(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Количество монет не может быть отрицательным");
        }
        this.coins += amount;
    }


    // Основные методы игрока
    public void incrementGamesPlayed() {
        this.gamesPlayed++;
    }

    public void incrementWins() {
        this.wins++;
        this.currentStreak++;
    }

    public void incrementLoses() {
        this.loses++;
        this.currentStreak = 0;
    }


}
//
 //     Player player1;
 //     Player player2;
 //     player1.equals(player2);
 //     player2.equals(player1);   СУБД  (RDBMS)
 //





