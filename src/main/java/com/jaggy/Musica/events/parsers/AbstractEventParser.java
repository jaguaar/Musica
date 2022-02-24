package com.jaggy.Musica.events.parsers;

import com.jaggy.Musica.events.CommandEvent;
import net.dv8tion.jda.api.entities.Message;

public abstract class AbstractEventParser {

    protected final String PREFIX;
    protected final String CHANNEL;

    AbstractEventParser(final String PREFIX, final String CHANNEL) {
        this.PREFIX = PREFIX;
        this.CHANNEL = CHANNEL;
    }

    public boolean matches(final Message message) {
        return theAuthorIsNotABot(message) && isChannel(message);
    }

    public abstract CommandEvent parseCommandEvent(final Message message);

    boolean theAuthorIsNotABot(final Message message) {
        return !message.getAuthor().isBot();
    }

    boolean isChannel(final Message message) {
        return message.getChannel().getName().toLowerCase().contains(CHANNEL);
    }
}
