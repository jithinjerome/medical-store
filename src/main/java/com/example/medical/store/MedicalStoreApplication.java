package com.example.medical.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MedicalStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicalStoreApplication.class, args);
	}

}
