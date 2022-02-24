package com.jaggy.Musica.events;

import com.jaggy.Musica.events.parsers.AbstractEventParser;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Async
@Component
public class MessageEventListenerImpl extends ListenerAdapter implements MessageEventListener {

    private final List<AbstractEventParser> parsers;
    private final List<CommandEventListener> listeners;

    public MessageEventListenerImpl(
            @Qualifier("CommandEventParser") final AbstractEventParser parser,
            @Qualifier("ShortcutEventParser") final AbstractEventParser shortcutParser,
            @Qualifier("GifEventParser") final AbstractEventParser gifParser,
            @Lazy final List<CommandEventListener> listeners) {
        this.listeners = listeners;
        this.parsers = List.of(parser, shortcutParser, gifParser);
    }

    @Override
    public void onGuildMessageReceived(final GuildMessageReceivedEvent event) {
        processMessage(event.getMessage());
    }

    @Override
    public void onPrivateMessageReceived(@NotNull final PrivateMessageReceivedEvent event) {
        processMessage(event.getMessage());
    }

    private void processMessage(final Message message) {
        parsers.stream()
                .filter(abstractEventParser -> abstractEventParser.matches(message))
                .map(abstractEventParser -> abstractEventParser.parseCommandEvent(message))
                .forEach(commandEvent -> listeners.forEach(l -> l.onCommandEvent(commandEvent)));
    }
    
}
