package com.naperstky.player;

import com.naperstky.security.UserAccount;
import jakarta.persistence.*;

    @Entity
@Table(name ="players")
public class Player {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long id;



        @OneToOne
        @JoinColumn(name = "user_account_id", referencedColumnName = "id")
        private UserAccount userAccount;
        private int score = 0;          // Общий счёт
        private int gamesPlayed = 0;    // Количество игр
        private int wins = 0;           // Победы
        private int currentStreak = 0;














}
