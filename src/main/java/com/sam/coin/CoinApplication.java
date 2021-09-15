package com.sam.coin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sam.coin.api.CoinController;

@SpringBootApplication
public class CoinApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoinApplication.class, args);
	}

}