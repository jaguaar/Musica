package com.jaggy.Musica.discord;

import net.dv8tion.jda.api.events.GenericEvent;
import org.springframework.scheduling.annotation.Async;

@Async
public interface MessageHandler {
	void handleMessage(GenericEvent event);
}
