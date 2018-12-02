package com.cobee.registrydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class RegistrydemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegistrydemoApplication.class, args);
	}
}
