package com.jaggy.Musica.events.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jaggy.Musica.events.CommandEvent;

import net.dv8tion.jda.api.entities.Message;

@Component(value = "ShortcutEventParser")
public class ShortcutEventParser extends AbstractEventParser {

	private final Logger LOG = LogManager.getLogger(ShortcutEventParser.class);

	private final Properties shortcutList = new Properties();

	public ShortcutEventParser(@Value("${command.prefix}") final String PREFIX, @Value("${command.channel}") final String CHANNEL) {
		super(PREFIX, CHANNEL);
		loadShortcutList();
		predicate.and(isShortCutOrSpotifyLink());
	}

	@Override
	public CommandEvent parseCommandEvent(final Message message) {
		if (isShortCut().test(message)) {
			return new CommandEvent(message, "play", List.of(shortcutList.getProperty(message.getContentRaw())));
		} else if (isSpotifyLink().test(message)) {
			return new CommandEvent(message, "play", List.of(message.getContentRaw()));
		}
		return null;
	}

	private Predicate<Message> isShortCutOrSpotifyLink() {
		return isShortCut().or(isSpotifyLink());
	}

	private Predicate<Message> isShortCut() {
		return message -> shortcutList.containsKey(message.getContentRaw());
	}

	private Predicate<Message> isSpotifyLink() {
		return message -> message.getContentRaw().matches("^https:\\/\\/open.spotify.com[\\S]*\\Z");
	}

	private void loadShortcutList() {
		try (final InputStream input = ShortcutEventParser.class.getClassLoader().getResourceAsStream("shortcut.properties")) {
			shortcutList.load(input);
		} catch (final IOException e) {
			LOG.error("Could not load short cut list", e);
		}
	}

	public Properties getShortcutList() {
		return shortcutList;
	}

}
