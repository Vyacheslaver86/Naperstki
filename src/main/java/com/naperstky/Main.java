package com.naperstky;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.naperstky")
@EntityScan(basePackages = "com.naperstky")
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Тест БД (оставляем)
    @Bean
    public CommandLineRunner testDatabase(DataSource dataSource) {
        return args -> {
            System.out.println("\n=== Тест подключения к PostgreSQL ===");
            try (Connection conn = dataSource.getConnection()) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("✅ Успешное подключение!");
                System.out.println("URL: " + meta.getURL());
                System.out.println("Пользователь: " + meta.getUserName());
            } catch (Exception e) {
                System.err.println("❌ Ошибка подключения: " + e.getMessage());
            }
        };
    }

    // Веб-контроллер (новое!)
    @Bean
    public CommandLineRunner initWeb() {
        return args -> {
            System.out.println("\n=== Веб-доступ ===");
            System.out.println("URL: http://localhost:8081/");
            System.out.println("Swagger: http://localhost:8081/swagger-ui.html");
        };
    }
}