package com.jaggy.Musica.events.parsers;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import com.jaggy.Musica.events.CommandEvent;

import kotlin.Pair;
import net.dv8tion.jda.api.entities.Message;

public abstract class AbstractEventParser {

	protected final String PREFIX;
	protected final String CHANNEL;
	protected Predicate<Message> predicate;

	AbstractEventParser(final String PREFIX, final String CHANNEL) {
		this.PREFIX = PREFIX;
		this.CHANNEL = CHANNEL;
		this.predicate = m -> theAuthorIsNotABot(m) && isInChannel(m);
	}

	public final boolean matches(final Message message) {
		return predicate.test(message);
	}

	protected final Pair<String, List<String>> parseContentRaw(final String contentRaw) {
		final String[] split = contentRaw.split("\\s");
		return switch (split.length) {
		case 1 -> new Pair(split[0].substring(PREFIX.length()), List.of());
		default -> new Pair(split[0].substring(PREFIX.length()), Arrays.asList(split).subList(1, split.length));
		};
	}

	public abstract CommandEvent parseCommandEvent(final Message message);

	boolean theAuthorIsNotABot(final Message message) {
		return !message.getAuthor().isBot();
	}

	boolean isInChannel(final Message message) {
		return message.getChannel().getName().toLowerCase().contains(CHANNEL);
	}
}
