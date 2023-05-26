package com.jaggy.Musica.events;

import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Async
@Component
public class GuildEventListenerImpl extends ListenerAdapter implements GuildEventListener {
    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        super.onGuildLeave(event);
        if(event.getGuild().getMembers().size() < 2) {
            final var voiceChannel = event.getGuild().getSelfMember().getVoiceState().getChannel();
            if (voiceChannel != null) {
                event.getGuild().getAudioManager().closeAudioConnection();
            }
        }
    }
}
