package com.jaggy.Musica.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;

@Service
public class DiscordClient {
	@Value("discord.token")
	private String token;

	private final JDA discordWrapper;

	@Autowired
	public DiscordClient(MessageListener messageListener) throws LoginException {
		discordWrapper = JDABuilder
				.createDefault(token)
				.addEventListeners(messageListener)
				.build();
	}
}
