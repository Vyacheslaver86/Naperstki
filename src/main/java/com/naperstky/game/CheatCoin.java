package com.naperstky.game;

import jakarta.persistence.*;

@Embeddable
public class CheatCoin {


        public CheatCoin() {}





    @Enumerated(EnumType.STRING)

        private CheatType type;  // Тип жульничества
    @Column(name ="is used")
    private boolean used;    // Использована ли монетка

        public CheatCoin(CheatType type) {
            this.type = type;
            this.used = false;
        }

        // --- Геттеры и сеттеры ---
        public CheatType getType() {
            return type;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }
    }




