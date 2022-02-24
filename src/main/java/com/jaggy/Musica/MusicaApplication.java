package com.jaggy.Musica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MusicaApplication {

	private final JaggyBot jaggyBot;

	@Autowired
	public MusicaApplication(final JaggyBot jaggyBot) {
		this.jaggyBot = jaggyBot;
	}

	public static void main(String[] args) {
		SpringApplication.run(MusicaApplication.class, args);
	}


	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println("Started Bot");
		};
	}
}
