package com.jaggy.Musica.events;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jaggy.Musica.events.parsers.CommandEventParser;

import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.internal.entities.ReceivedMessage;
import net.dv8tion.jda.internal.entities.SelfUserImpl;

public class CommandEventParserTest {

	private CommandEventParser parse;
	private ReceivedMessage receivedMessage;

	@BeforeEach
	public void setUp() {
		parse = new CommandEventParser(0, ".", "musica");
		receivedMessage = mock(ReceivedMessage.class);
	}

	@Test
	void playYoutube() {
		mockMessage(".p youtube.com");
		final CommandEvent event = parse.parseCommandEvent(receivedMessage);

		assertThat(event.getAction()).isEqualTo("p");
		assertThat(event.getArgs().get(0)).isEqualTo("youtube.com");
	}

	@Test
	void playCommand() {
		mockMessage(".play command");
		final CommandEvent event = parse.parseCommandEvent(receivedMessage);

		assertThat(event.getAction()).isEqualTo("play");
		assertThat(event.getArgs().get(0)).isEqualTo("command");
	}

	private void mockMessage(final String message) {
		when(receivedMessage.getContentRaw()).thenReturn(message);
		when(receivedMessage.getAuthor()).thenReturn(new SelfUserImpl(1L, null));
		final MessageChannelUnion textChannel = mock(MessageChannelUnion.class);
		when(textChannel.getName()).thenReturn("musica");
		when(receivedMessage.getChannel()).thenReturn(textChannel);
	}
}
