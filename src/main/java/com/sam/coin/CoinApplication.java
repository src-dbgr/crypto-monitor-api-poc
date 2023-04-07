package com.sam.coin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sam.coin.api.CoinController;

import java.util.TimeZone;

@SpringBootApplication
public class CoinApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(CoinApplication.class, args);
    }

}