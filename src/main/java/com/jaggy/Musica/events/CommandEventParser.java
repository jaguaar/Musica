package com.jaggy.Musica.events;

import net.dv8tion.jda.api.entities.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Component
public class CommandEventParser {

    private final String PREFIX;

    private final Properties shortcutList = new Properties();

    public CommandEventParser(@Value("${command.prefix}") final String PREFIX) {
        this.PREFIX = PREFIX;
        loadShortcutList();
    }

    public Optional<CommandEvent> parseCommandEvent(final Message message) {
        if (theAuthorIsNotABot(message)) {
            if (startsWithPrefix(message)) {
                final String contentRaw = message.getContentRaw();
                final String[] split = contentRaw.split("\\s");
                return switch (split.length) {
                    case 1 -> Optional.of(new CommandEvent(message, split[0].substring(PREFIX.length()), List.of()));
                    default -> Optional.of(new CommandEvent(message, split[0].substring(PREFIX.length()), Arrays.asList(split).subList(1, split.length)));
                };
            } else if (isSpotifyLink(message)) {
                return Optional.of(new CommandEvent(message, "play", List.of(message.getContentRaw())));
            } else if (isShortcutPlaylistQueue(message)) {
                return Optional.of(new CommandEvent(message, "play", List.of(shortcutList.getProperty(message.getContentRaw()))));
            }
        }
        return Optional.empty();
    }

    private boolean isSpotifyLink(final Message message) {
        return message.getContentRaw().matches("^https:\\/\\/open.spotify.com[\\S]*\\Z");
    }

    private boolean isShortcutPlaylistQueue(final Message message) {
        return shortcutList.containsKey(message.getContentRaw());
    }

    private boolean startsWithPrefix(final Message message) {
        return message.getContentRaw().startsWith(PREFIX);
    }

    private boolean theAuthorIsNotABot(final Message message) {
        return !message.getAuthor().isBot();
    }

    private void loadShortcutList() {
        try (final InputStream input = CommandEventParser.class.getClassLoader().getResourceAsStream("shortcut.properties")) {
            shortcutList.load(input);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    protected Properties getShortcutList() {
        return shortcutList;
    }

}
