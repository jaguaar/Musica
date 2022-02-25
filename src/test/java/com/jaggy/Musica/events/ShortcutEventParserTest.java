package com.jaggy.Musica.events;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jaggy.Musica.events.parsers.ShortcutEventParser;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.internal.entities.ReceivedMessage;
import net.dv8tion.jda.internal.entities.SelfUserImpl;

public class ShortcutEventParserTest {

	private ShortcutEventParser parse;
	private ReceivedMessage receivedMessage;

	@BeforeEach
	public void setUp() {
		parse = new ShortcutEventParser(".", "musica");
		receivedMessage = mock(ReceivedMessage.class);
	}

	@Test
	void playShortcutBeer() {
		testShortcut("beer");
	}

	@Test
	void playShortcutBunker() {
		testShortcut("bunker");
	}

	@Test
	void playShortcutHater() {
		testShortcut("annoy_jerre");
	}

	private void testShortcut(final String test) {
		mockMessage(test);
		final CommandEvent event = parse.parseCommandEvent(receivedMessage);

		assertThat(event.getAction()).isEqualTo("play");
		assertThat(event.getArgs().get(0)).isEqualTo(parse.getShortcutList().get(test));
	}

	@Test
	void playSpotifyLink() {
		final String spotifyLink = "https://open.spotify.com/playlist/37i9dQZF1DXauOWFg72pbl?si=f3bcf7d855274a61";
		mockMessage(spotifyLink);
		final CommandEvent event = parse.parseCommandEvent(receivedMessage);

		assertThat(event.getAction()).isEqualTo("play");
		assertThat(event.getArgs().get(0)).isEqualTo(spotifyLink);
	}

	private void mockMessage(final String message) {
		when(receivedMessage.getContentRaw()).thenReturn(message);
		when(receivedMessage.getAuthor()).thenReturn(new SelfUserImpl(1L, null));
		final TextChannel textChannel = mock(TextChannel.class);
		when(textChannel.getName()).thenReturn("musica");
		when(receivedMessage.getChannel()).thenReturn(textChannel);
	}
}
