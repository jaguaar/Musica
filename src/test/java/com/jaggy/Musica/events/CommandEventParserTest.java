package com.jaggy.Musica.events;

import com.jaggy.Musica.events.parsers.CommandEventParser;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.internal.entities.ReceivedMessage;
import net.dv8tion.jda.internal.entities.SelfUserImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandEventParserTest {

    private CommandEventParser parse;
    private ReceivedMessage receivedMessage;

    @BeforeEach
    public void setUp() {
        parse = new CommandEventParser(".", "musica");
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