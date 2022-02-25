package com.jaggy.Musica.events;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.jaggy.Musica.events.parsers.AbstractEventParser;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Async
@Component
public class MessageEventListenerImpl extends ListenerAdapter implements MessageEventListener {

	private final Logger LOG = LogManager.getLogger(MessageEventListenerImpl.class);

	private final List<AbstractEventParser> parsers;
	private final List<CommandEventListener> listeners;

	public MessageEventListenerImpl(final List<AbstractEventParser> parsers,
			@Lazy final List<CommandEventListener> listeners) {
		this.parsers = parsers;
		this.listeners = listeners;
	}

	@Override
	public void onGuildMessageReceived(final GuildMessageReceivedEvent event) {
		LOG.debug("Guild message received from {}", event.getAuthor());
		processMessage(event.getMessage());
	}

	@Override
	public void onPrivateMessageReceived(@NotNull final PrivateMessageReceivedEvent event) {
		LOG.debug("Private message received from {}", event.getAuthor());
		processMessage(event.getMessage());
	}

	private void processMessage(final Message message) {
		parsers.stream()
				.filter(abstractEventParser -> abstractEventParser.matches(message))
				.map(abstractEventParser -> abstractEventParser.parseCommandEvent(message))
				.forEach(commandEvent -> listeners.forEach(l -> l.onCommandEvent(commandEvent)));
	}

}
