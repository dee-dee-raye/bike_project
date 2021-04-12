package com.globomatics.bike;

import com.globomatics.bike.models.BikeUserDetails;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import java.util.Optional;
import java.util.function.BiFunction;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BikeApplication {

	@Bean
	BiFunction<Optional<BikeUserDetails>, String, Boolean> owner() {
		return (bikeUserDetail, username) -> bikeUserDetail
				.filter(b -> b.getUsername().equals(username))
				.isPresent();
	}

	public static void main(String[] args) {
		SpringApplication.run(BikeApplication.class, args);
	}

}
