package com.loga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class LogaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogaBackendApplication.class, args);
    }
}
