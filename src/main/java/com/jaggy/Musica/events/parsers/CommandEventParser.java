package com.jaggy.Musica.events.parsers;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jaggy.Musica.events.CommandEvent;

import net.dv8tion.jda.api.entities.Message;

@Component(value = "CommandEventParser")
public class CommandEventParser extends AbstractEventParser {

	public CommandEventParser(@Value("${command.prefix}") final String PREFIX, @Value("${command.channel}") final String CHANNEL) {
		super(PREFIX, CHANNEL);
		predicate.and(startsWithCommandPrefix());
	}

	@Override
	public CommandEvent parseCommandEvent(final Message message) {
		final String contentRaw = message.getContentRaw();
		return parseCommandString(message, contentRaw);
	}

	CommandEvent parseCommandString(final Message message, final String contentRaw) {
		final String[] split = contentRaw.split("\\s");
		return switch (split.length) {
		case 1 -> new CommandEvent(message, split[0].substring(PREFIX.length()), List.of());
		default -> new CommandEvent(message, split[0].substring(PREFIX.length()), Arrays.asList(split).subList(1, split.length));
		};
	}

	private Predicate<Message> startsWithCommandPrefix() {
		return message -> message.getContentRaw().startsWith(PREFIX);
	}

}
