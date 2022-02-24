package com.jaggy.Musica.events;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.dv8tion.jda.api.entities.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommandEventParser {
	private String PREFIX;

	public CommandEventParser(@Value("${command.prefix}") String PREFIX) {
		this.PREFIX = PREFIX;
	}

	public Optional<CommandEvent> parseCommandEvent(final Message message) {
		if (theAuthorIsNotABot(message) && startsWithPrefix(message)) {
			final String contentRaw = message.getContentRaw();
			final String[] split = contentRaw.split("\\s");
			return switch (split.length) {
				case 1 -> Optional.of(new CommandEvent(message, split[0].substring(PREFIX.length()), List.of()));
				default -> Optional.of(new CommandEvent(message, split[0].substring(PREFIX.length()), Arrays.asList(split).subList(1, split.length)));
			};
		}
		return Optional.empty();
	}

	private boolean startsWithPrefix(final Message message) {
		return message.getContentRaw().startsWith(PREFIX);
	}

	private boolean theAuthorIsNotABot(final Message message){
		return !message.getAuthor().isBot();
	}
}
