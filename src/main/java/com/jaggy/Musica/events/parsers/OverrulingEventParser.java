package com.jaggy.Musica.events.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.random.RandomGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.jaggy.Musica.events.CommandEvent;

import kotlin.Pair;
import net.dv8tion.jda.api.entities.Message;

public class OverrulingEventParser extends AbstractEventParser {
	private final Logger LOG = LogManager.getLogger(OverrulingEventParser.class);
	private final Properties overrules = new Properties();
	private final boolean enabled;
	private final int factor;

	public OverrulingEventParser(@Value("${overruling.event.parser.order}") final int order, @Value("${command.prefix}") final String PREFIX,
			@Value("${command.channel}") final String CHANNEL, @Value("${overruling.event.parser.enabled}") final boolean enabled,
			@Value("${overruling.event.parser.random.factor}") final int factor) {
		super(order, PREFIX, CHANNEL);
		this.enabled = enabled;
		this.factor = factor;
		this.predicate = predicate.and(shouldOverrule());
		if (enabled) {
			loadOverrules();
		}
	}

	private Predicate<? super Message> shouldOverrule() {
		return message -> {
			if (!enabled) {
				return false;
			}
			if (RandomGenerator.getDefault().nextInt() % factor != 0) {
				return false;
			}
			final Pair<String, List<String>> parsed = parseContentRaw(message.getContentRaw());
			if (!List.of("p", "pn", "play", "playnext").contains(parsed.getFirst())) {
				return false;
			}
			if (!overrules.containsKey(message.getAuthor().getName())) {
				return false;
			}
			return true;
		};
	}

	private void loadOverrules() {
		try (final InputStream input = OverrulingEventParser.class.getClassLoader().getResourceAsStream("overrules.properties")) {
			overrules.load(input);
		} catch (final IOException e) {
			LOG.error("Could not load overrules list", e);
		}
	}

	@Override
	public CommandEvent parseCommandEvent(final Message message) {
		final Pair<String, List<String>> parsed = parseContentRaw(message.getContentRaw());
		final String overrule = overrules.getProperty(message.getAuthor().getName());
		return new CommandEvent(message, parsed.getFirst(), List.of(overrule));
	}
}
