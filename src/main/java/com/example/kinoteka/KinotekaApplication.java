package com.example.kinoteka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.kinoteka.dao.repositories")
public class KinotekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(KinotekaApplication.class, args);
    }

}
