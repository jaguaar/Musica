package com.jaggy.Musica.events;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jaggy.Musica.events.parsers.GifEventParser;

import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.internal.entities.ReceivedMessage;
import net.dv8tion.jda.internal.entities.SelfUserImpl;

public class GifEventParserTest {

	private GifEventParser parse;
	private ReceivedMessage receivedMessage;

	@BeforeEach
	public void setUp() {
		parse = new GifEventParser(0, ".", "musica");
		receivedMessage = mock(ReceivedMessage.class);
	}

	@Test
	void playBeerGif() {
		mockMessage("https://tenor.com/view/will-ferrell-old-milwaukee-beer-drink-party-gif-22890849");
		final CommandEvent event = parse.parseCommandEvent(receivedMessage);
		assertThat(event.getAction()).isEqualTo("play");
		assertThat(event.getArgs().get(0)).isEqualTo("https://open.spotify.com/playlist/37i9dQZF1DXauOWFg72pbl?si=f3bcf7d855274a61");
	}

	@Test
	void playBenji() {
		mockMessage("https://gfycat.com/astonishingshrillcopperhead");
		final CommandEvent event = parse.parseCommandEvent(receivedMessage);
		assertThat(event.getAction()).isEqualTo("clear");
		assertThat(event.getArgs().isEmpty()).isTrue();
	}

	private void mockMessage(final String message) {
		when(receivedMessage.getContentRaw()).thenReturn(message);
		when(receivedMessage.getAuthor()).thenReturn(new SelfUserImpl(1L, null));
		final MessageChannelUnion textChannel = mock(MessageChannelUnion.class);
		when(textChannel.getName()).thenReturn("musica");
		when(receivedMessage.getChannel()).thenReturn(textChannel);
	}
}