package com.naperstky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
@EntityScan("com.yourpackage.model")
public class Main{
    public static void main(String[] args) {

        SpringApplication.run(Main.class);




    }
}
