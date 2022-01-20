package com.jaggy.Musica;

import com.jaggy.Musica.discord.DiscordClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MusicaApplication {

	@Autowired
	public MusicaApplication(final DiscordClient discordClient) {
		this.discordClient = discordClient;
	}

	public static void main(String[] args) {
		SpringApplication.run(MusicaApplication.class, args);
	}

	private final DiscordClient discordClient;

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println("Started Bot");
		};
	}
}
