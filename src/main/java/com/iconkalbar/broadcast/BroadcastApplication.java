package com.iconkalbar.broadcast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BroadcastApplication {

	public static void main(String[] args) {
		SpringApplication.run(BroadcastApplication.class, args);
	}

}
