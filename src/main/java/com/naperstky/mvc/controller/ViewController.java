package com.naperstky.mvc.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String homePage() {
        return "forward:/index.html";
    }

    // Можно оставить для других страниц если нужно
    @GetMapping("/game")
    public String gamePage() {
        return "forward:/index.html";
    }
}








