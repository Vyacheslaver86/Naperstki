package com.naperstky.mvc.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
    public class ViewController {

        @GetMapping("/register")
        public String registerPage() {
            return "register.html";
        }

        @GetMapping("/login")
        public String loginPage() {
            return "login.html";
        }

        @GetMapping("/")
        public String homePage() {
            return "index.html";
        }
    }









