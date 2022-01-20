package com.jaggy.Musica.discord;

import org.springframework.stereotype.Component;

@Component
public class MessageUtils {
	public String getCommand(final String message) {
		if(message.contains(" ")) {
			return message.substring(1, message.indexOf(" "));
		}

		return message.substring(1);
	}

	public String getUrl(final String message) {
		if(message.contains(" ")) {
			return message.substring(message.indexOf(" ")+1);
		}

		return "";
	}
}
