package com.finnplay.user.manager.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UserManagerAppApplication {

	@Bean
	public PasswordEncoder passwordEncoder(@Value("${password.bcrypt.version}") BCryptPasswordEncoder.BCryptVersion version,
										   @Value("${password.bcrypt.strength}") int strength) {
		return new BCryptPasswordEncoder(version, strength);
	}

	public static void main(String[] args) {
		SpringApplication.run(UserManagerAppApplication.class, args);
	}

}
