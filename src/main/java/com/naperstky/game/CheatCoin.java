package com.naperstky.game;

import jakarta.persistence.*;

@Embeddable
public class CheatCoin {

    @Enumerated(EnumType.STRING)
    private CheatType type;

    @Column(name = "is_used")
    private boolean used;

    public CheatCoin() {}

    public CheatCoin(CheatType type) {
        this.type = type;
        this.used = false;
    }

    public CheatType getType() {
        return type;
    }

    public void setType(CheatType type) {
        this.type = type;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}