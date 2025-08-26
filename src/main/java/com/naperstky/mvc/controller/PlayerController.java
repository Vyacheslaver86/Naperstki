package com.naperstky.mvc.controller;


import com.naperstky.mvc.service.PlayerService;
import com.naperstky.player.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/players")
public class PlayerController {
private final PlayerService playerService;

    @GetMapping("/info")
    public ResponseEntity<Player> getPlayerByUserId(@RequestParam("user_id") Long userId) {
        try {
            Player player = playerService.getPlayerByUser(userId);
            return ResponseEntity.ok(player);
        } catch (ResponseStatusException ex) {
            throw ex;  // Пробрасываем существующие ошибки
        } catch (Exception ex) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Error retrieving player data");
        }
    }




    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Player controller is working!");
    }
}










