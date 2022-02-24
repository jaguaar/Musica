package com.jaggy.Musica.events;

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
        parse = new CommandEventParser(".", "jordy-discord-bot");
        receivedMessage = mock(ReceivedMessage.class);
    }

    @Test
    void playYoutube() {
        mockMessage(".p youtube.com");
        final CommandEvent event = parse.parseCommandEvent(receivedMessage).orElseThrow();

        assertThat(event.getAction()).isEqualTo("p");
        assertThat(event.getArgs().get(0)).isEqualTo("youtube.com");
    }

    @Test
    void playCommand() {
        mockMessage(".play command");
        final CommandEvent event = parse.parseCommandEvent(receivedMessage).orElseThrow();

        assertThat(event.getAction()).isEqualTo("play");
        assertThat(event.getArgs().get(0)).isEqualTo("command");
    }

    @Test
    void playSpotifyLink() {
        final String spotifyLink = "https://open.spotify.com/playlist/37i9dQZF1DXauOWFg72pbl?si=f3bcf7d855274a61";
        mockMessage(spotifyLink);
        final CommandEvent event = parse.parseCommandEvent(receivedMessage).orElseThrow();

        assertThat(event.getAction()).isEqualTo("play");
        assertThat(event.getArgs().get(0)).isEqualTo(spotifyLink);
    }

    @Test
    void playShortcutBeer() {
        testShortcut("beer");
    }

    @Test
    void playShortcutBunker() {
        testShortcut("bunker");
    }

    private void testShortcut(final String beer) {
        final String command = beer;
        mockMessage(command);
        final CommandEvent event = parse.parseCommandEvent(receivedMessage).orElseThrow();

        assertThat(event.getAction()).isEqualTo("play");
        assertThat(event.getArgs().get(0)).isEqualTo(parse.getShortcutList().get(command));
    }

    private void mockMessage(final String message) {
        when(receivedMessage.getContentRaw()).thenReturn(message);
        when(receivedMessage.getAuthor()).thenReturn(new SelfUserImpl(1l, null));
        final TextChannel textChannel = mock(TextChannel.class);
        when(textChannel.getName()).thenReturn("jordy-discord-bot");
        when(receivedMessage.getChannel()).thenReturn(textChannel);
    }
}