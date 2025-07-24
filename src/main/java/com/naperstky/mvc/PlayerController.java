package com.naperstky.mvc;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {


    @GetMapping
    public String testMethod() {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("PROGRAM RUNNING");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        return "hello";
    }

}
