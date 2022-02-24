package com.jaggy.Musica.events.parsers;

import com.jaggy.Musica.events.CommandEvent;
import net.dv8tion.jda.api.entities.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

@Component(value = "ShortcutEventParser")
public class ShortcutEventParser extends AbstractEventParser {

    private final Properties shortcutList = new Properties();

    public ShortcutEventParser(@Value("${command.prefix}") final String PREFIX, @Value("${command.channel}") final String CHANNEL) {
        super(PREFIX, CHANNEL);
        loadShortcutList();
    }

    @Override
    public boolean matches(final Message message) {
        return super.matches(message) && isShortcutPlaylistQueue(message);
    }

    @Override
    public CommandEvent parseCommandEvent(final Message message) {
        if (matches(message)) {
            return new CommandEvent(message, "play", List.of(shortcutList.getProperty(message.getContentRaw())));
        }
        return null;
    }

    private boolean isShortcutPlaylistQueue(final Message message) {
        return shortcutList.containsKey(message.getContentRaw());
    }

    private void loadShortcutList() {
        try (final InputStream input = GifEventParser.class.getClassLoader().getResourceAsStream("shortcut.properties")) {
            shortcutList.load(input);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public Properties getShortcutList() {
        return shortcutList;
    }

}
