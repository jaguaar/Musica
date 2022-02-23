package com.jaggy.Musica.discord;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.scheduling.annotation.Async;

@Async
public interface MessageHandler {
	void handleMessage(GenericEvent event);

	static void sendMessage(final String message, final GuildMessageReceivedEvent event) {
		event.getChannel().sendMessage(message).queue();
	}
}
