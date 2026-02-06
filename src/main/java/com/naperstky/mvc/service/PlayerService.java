package com.naperstky.mvc.service;

import com.naperstky.mvc.repository.PlayerDAO;
import com.naperstky.player.Player;
import com.naperstky.security.UserAccount;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerDAO playerDAO;





    public Player getPlayerByUser(Long userId) {
        return playerDAO.findByUserAccountId(userId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Player not found for user ID"+userId));
    }

    public Player getPlayerByUserWithCreation(UserAccount user) {
        return playerDAO.findByUserAccountId(user.getId())
                .orElseGet(() -> {
                    Player player = new Player();
                    player.setUserAccount(user);
                    player.setCoins(10);
                    return playerDAO.save(player);
                });
    }

    public void updatePlayerStats(Player player, boolean isWin) {
        if (isWin) {
            player.incrementWins();
            player.addCoins(20);
        } else {
            player.incrementLoses();
        }
        playerDAO.save(player);
    }
}