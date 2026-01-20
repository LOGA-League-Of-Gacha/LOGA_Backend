package com.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class LogBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogBackendApplication.class, args);
    }
}
