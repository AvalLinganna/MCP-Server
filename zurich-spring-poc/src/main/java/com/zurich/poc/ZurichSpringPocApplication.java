package com.zurich.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZurichSpringPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZurichSpringPocApplication.class, args);
    }
}
