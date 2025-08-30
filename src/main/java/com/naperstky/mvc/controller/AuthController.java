package com.naperstky.mvc.controller;

import com.naperstky.security.JwtTokenUtil;
import com.naperstky.security.UserAccount;
import com.naperstky.security.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")

public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ✅ Оба record должны быть объявлены
    public static record LoginRequest(String username, String password) {}
    public static record RegisterRequest(String username, String password, String nickname) {} // ← ДОБАВЬТЕ ЭТО

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtTokenUtil,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ... остальной код


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),  // ← правильно для record
                            request.password()   // ← правильно для record
                    )
            );

            UserAccount user = (UserAccount) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(user);

            return ResponseEntity.ok(Map.of("token", token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Неверные учетные данные");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка сервера: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            if (userRepository.existsByUsername(request.username())) {
                return ResponseEntity.badRequest().body("Username already exists");
            }

            UserAccount user = new UserAccount();
            user.setUsername(request.username());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setNickname(request.nickname() != null ? request.nickname() : request.username());

            // ✅ ЯВНО УСТАНАВЛИВАЕМ РОЛИ
            user.setRoles(new HashSet<>(Set.of("USER")));

            userRepository.save(user);

            // ✅ ПРОВЕРКА
            System.out.println("User ID: " + user.getId());
            System.out.println("Roles: " + user.getRoles());

            return ResponseEntity.ok("User registered successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }}