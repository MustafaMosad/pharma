package com.appsstuff.pharma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PharmaApplication {

	public static void main(String[] args) {

		SpringApplication.run(PharmaApplication.class, args);

	}

}
