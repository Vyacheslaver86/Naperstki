package com.naperstky.mvc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.naperstky.player.Player;

import java.util.Optional;

@Repository
public interface PlayerDAO extends JpaRepository<Player,Long> {

        Optional<Player> findByUserAccountId(Long userAccountId); // Ищем по ID аккаунта
    }





