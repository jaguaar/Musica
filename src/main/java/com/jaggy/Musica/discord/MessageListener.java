package com.jaggy.Musica.discord;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageListener implements EventListener {
	private final MessageHandler messageHandler;

	@Autowired
	public MessageListener(final MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	@Override
	public void onEvent(GenericEvent event) {
		if (event instanceof GuildMessageReceivedEvent) {
			messageHandler.handleMessage(event);
		}
	}
}
