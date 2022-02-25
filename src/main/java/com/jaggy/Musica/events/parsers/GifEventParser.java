package com.jaggy.Musica.events.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jaggy.Musica.events.CommandEvent;

import net.dv8tion.jda.api.entities.Message;

@Component(value = "GifEventParser")
public class GifEventParser extends CommandEventParser {

	private final Logger LOG = LogManager.getLogger(GifEventParser.class);

	private static final String COMMA_DELIMITER = ",";

	private HashMap<String, String> gifCommand;

	public GifEventParser(@Value("${command.prefix}") final String PREFIX, @Value("${command.channel}") final String CHANNEL) {
		super(PREFIX, CHANNEL);
		loadGifList();
		predicate.and(isKnownGif());
	}

	@Override
	public CommandEvent parseCommandEvent(final Message message) {
		if (matches(message)) {
			final String contentRaw = gifCommand.get(message.getContentRaw());
			return parseCommandString(message, contentRaw);
		}
		return null;
	}

	private Predicate<Message> isKnownGif() {
		return message -> gifCommand.containsKey(message.getContentRaw());
	}

	private void loadGifList() {
		gifCommand = new HashMap<>();
		try (final BufferedReader br = new BufferedReader(
				new InputStreamReader(GifEventParser.class.getClassLoader().getResourceAsStream("gif.csv")))) {
			String line;
			br.readLine();
			while ((line = br.readLine()) != null) {
				final String[] values = line.split(COMMA_DELIMITER);
				gifCommand.put(values[0], values[1]);
			}
		} catch (final IOException e) {
			LOG.error("Could not read gif list", e);
		}
	}

}
