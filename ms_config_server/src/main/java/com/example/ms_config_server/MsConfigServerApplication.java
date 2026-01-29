package com.example.ms_config_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.config.server.config.ConfigServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@SpringBootApplication
@EnableConfigServer
public class MsConfigServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(MsConfigServerApplication.class, args);
	}
}
