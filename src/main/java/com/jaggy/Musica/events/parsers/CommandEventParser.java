package com.jaggy.Musica.events.parsers;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jaggy.Musica.events.CommandEvent;

import kotlin.Pair;
import net.dv8tion.jda.api.entities.Message;

@Component(value = "CommandEventParser")
public class CommandEventParser extends AbstractEventParser {

	public CommandEventParser(@Value("${command.prefix}") final String PREFIX, @Value("${command.channel}") final String CHANNEL) {
		super(PREFIX, CHANNEL);
		this.predicate = predicate.and(startsWithCommandPrefix());
	}

	@Override
	public CommandEvent parseCommandEvent(final Message message) {
		final String contentRaw = message.getContentRaw();
		final Pair<String, List<String>> actionAndArgs = parseContentRaw(contentRaw);
		return new CommandEvent(message, actionAndArgs.getFirst(), actionAndArgs.getSecond());
	}

	private Predicate<Message> startsWithCommandPrefix() {
		return message -> message.getContentRaw().startsWith(PREFIX);
	}

}
