package com.example.ms_produit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsProduitApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsProduitApplication.class, args);
	}

}
