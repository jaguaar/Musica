package com.jaggy.Musica.events.parsers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.jaggy.Musica.events.CommandEvent;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.internal.entities.SelfUserImpl;

class OverrulingEventParserTest {
	@Test
	void given_disabled_when_matches_then_noMatch() {
		final OverrulingEventParser parser = new OverrulingEventParser(0, ".", "musica", false, 1);

		final boolean actual = parser.matches(mockMessage(".play youtube.com", "jerre"));

		assertThat(actual).isFalse();

	}

	@Test
	void given_enabledAndUserNotInOverruleList_when_matches_then_noMatch() {
		final OverrulingEventParser parser = new OverrulingEventParser(0, ".", "musica", true, 1);

		final boolean actual = parser.matches(mockMessage(".play youtube.com", "jerre"));

		assertThat(actual).isFalse();

	}

	@Test
	void given_enabledAndNotPlayCommand_when_matches_then_noMatch() {
		final OverrulingEventParser parser = new OverrulingEventParser(0, ".", "musica", true, 1);

		final boolean actual = parser.matches(mockMessage(".queue", "LVRkeuh"));

		assertThat(actual).isFalse();

	}

	@Test
	void given_enabledAndPlayCommandAndUsernameInOverruleList_when_matches_then_Match() {
		final OverrulingEventParser parser = new OverrulingEventParser(0, ".", "musica", true, 1);

		assertThat(parser.matches(mockMessage(".p youtube.com", "LVRkeuh"))).isTrue();
		assertThat(parser.matches(mockMessage(".play youtube.com", "LVRkeuh"))).isTrue();
		assertThat(parser.matches(mockMessage(".playnext youtube.com", "LVRkeuh"))).isTrue();
		assertThat(parser.matches(mockMessage(".pn youtube.com", "LVRkeuh"))).isTrue();

	}

	@Test
	void given_lars_when_parseCommandEvent_then_dana() {
		final OverrulingEventParser parser = new OverrulingEventParser(0, ".", "musica", true, 1);
		final CommandEvent commandEvent = parser.parseCommandEvent(mockMessage(".pn youtube.com", "LVRkeuh"));

		assertThat(commandEvent.getAction()).isEqualTo("pn");
		assertThat(commandEvent.getArgs()).containsExactly("https://www.youtube.com/watch?v=MI2-w9q_Lk4");
	}

	private Message mockMessage(final String content, final String userName) {
		final Message message = mock(Message.class);
		when(message.getContentRaw()).thenReturn(content);
		final SelfUserImpl selfUser = new SelfUserImpl(1L, null);
		selfUser.setName(userName);
		when(message.getAuthor()).thenReturn(selfUser);
		final MessageChannelUnion textChannel = mock(MessageChannelUnion.class);
		when(textChannel.getName()).thenReturn("musica");
		when(message.getChannel()).thenReturn(textChannel);

		return message;
	}
}