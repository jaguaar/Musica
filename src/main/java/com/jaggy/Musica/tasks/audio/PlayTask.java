package com.jaggy.Musica.tasks.audio;

import com.jaggy.Musica.JaggyBot;
import com.jaggy.Musica.tasks.Task;
import net.dv8tion.jda.api.entities.Message;

public class PlayTask implements Task {

    private final JaggyBot bot;

    private final Message message;
    private final String url;
    private final boolean next;

    public PlayTask(JaggyBot bot, Message message, String url, boolean next) {
        this.bot = bot;
        this.message = message;
        this.url = url;
        this.next = next;
    }

    @Override
    public void execute() {
        bot.getPlayHandler().play(message, url, next);
    }
}
