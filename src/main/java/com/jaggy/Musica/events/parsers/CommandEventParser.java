package com.jaggy.Musica.events.parsers;

import com.jaggy.Musica.events.CommandEvent;
import net.dv8tion.jda.api.entities.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component(value = "CommandEventParser")
public class CommandEventParser extends AbstractEventParser {

    public CommandEventParser(@Value("${command.prefix}") final String PREFIX, @Value("${command.channel}") final String CHANNEL) {
        super(PREFIX, CHANNEL);
    }

    @Override
    public boolean matches(final Message message) {
        return super.matches(message) && (startsWithPrefix(message) || isSpotifyLink(message));
    }

    public CommandEvent parseCommandEvent(final Message message) {
        if (startsWithPrefix(message)) {
            final String contentRaw = message.getContentRaw();
            return parseCommandString(message, contentRaw);
        } else if (isSpotifyLink(message)) {
            return new CommandEvent(message, "play", List.of(message.getContentRaw()));
        }
        return null;
    }

    CommandEvent parseCommandString(final Message message, final String contentRaw) {
        final String[] split = contentRaw.split("\\s");
        return switch (split.length) {
            case 1 -> new CommandEvent(message, split[0].substring(PREFIX.length()), List.of());
            default -> new CommandEvent(message, split[0].substring(PREFIX.length()), Arrays.asList(split).subList(1, split.length));
        };
    }

    private boolean isSpotifyLink(final Message message) {
        return message.getContentRaw().matches("^https:\\/\\/open.spotify.com[\\S]*\\Z");
    }

    private boolean startsWithPrefix(final Message message) {
        return message.getContentRaw().startsWith(PREFIX);
    }

}
