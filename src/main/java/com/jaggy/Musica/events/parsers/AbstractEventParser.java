package com.jaggy.Musica.events.parsers;

import java.util.function.Predicate;

import com.jaggy.Musica.events.CommandEvent;

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

	public abstract CommandEvent parseCommandEvent(final Message message);

	boolean theAuthorIsNotABot(final Message message) {
		return !message.getAuthor().isBot();
	}

	boolean isInChannel(final Message message) {
		return message.getChannel().getName().toLowerCase().contains(CHANNEL);
	}
}
