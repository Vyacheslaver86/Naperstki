package com.naperstky.mvc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.naperstky.player.Player;
@Repository
public interface PlayerDAO extends JpaRepository<Player,Long> {





}
