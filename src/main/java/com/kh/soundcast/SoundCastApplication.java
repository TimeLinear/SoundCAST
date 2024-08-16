package com.kh.soundcast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class SoundCastApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoundCastApplication.class, args);
	}

}
