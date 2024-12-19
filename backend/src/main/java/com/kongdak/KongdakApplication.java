package com.kongdak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KongdakApplication {

	public static void main(String[] args) {
		SpringApplication.run(KongdakApplication.class, args);
	}

}
