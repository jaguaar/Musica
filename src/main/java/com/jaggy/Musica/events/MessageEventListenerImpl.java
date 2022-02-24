package com.jaggy.Musica.events;

import java.util.List;
import java.util.Optional;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Async
@Component
public class MessageEventListenerImpl extends ListenerAdapter implements MessageEventListener {

	private final CommandEventParser parser;
	private final List<CommandEventListener> listeners;

	public MessageEventListenerImpl(CommandEventParser parser, @Lazy List<CommandEventListener> listeners) {
		this.parser = parser;
		this.listeners = listeners;
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		processMessage(event.getMessage());
	}

	@Override
	public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
		processMessage(event.getMessage());
	}

	private void processMessage(final Message message){
		final Optional<CommandEvent> command = parser.parseCommandEvent(message);
		command.ifPresent(commandEvent -> listeners.forEach(l -> l.onCommandEvent(commandEvent)));
	}
}
