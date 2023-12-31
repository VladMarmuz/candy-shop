package com.candyshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CandyShopApplication {

    public static void main(final String[] args) {
        SpringApplication.run(CandyShopApplication.class, args);
    }

}
