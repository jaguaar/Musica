package com.jaggy.Musica.events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public interface MessageEventListener {
    void onGuildMessageReceived(GuildMessageReceivedEvent event);
    void onPrivateMessageReceived(PrivateMessageReceivedEvent event);
}
