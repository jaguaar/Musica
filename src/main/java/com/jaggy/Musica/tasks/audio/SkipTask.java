package com.jaggy.Musica.tasks.audio;

import com.jaggy.Musica.JaggyBot;
import com.jaggy.Musica.tasks.Task;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Message;

public class SkipTask implements Task {

    private final JaggyBot bot;
    private final Message message;

    public SkipTask(JaggyBot bot, Message message) {
        this.bot = bot;
        this.message = message;
    }

    @Override
    public void execute() {
        AudioTrack audioTrack = bot.getPlayHandler().skip();
        message.getChannel().sendMessage(":track_next: Skipping " + audioTrack.getInfo().title + "!").queue();
    }
}
