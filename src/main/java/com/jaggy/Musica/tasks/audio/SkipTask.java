package com.jaggy.Musica.tasks.audio;

import com.jaggy.Musica.JaggyBot;
import com.jaggy.Musica.tasks.Task;
import net.dv8tion.jda.api.entities.Message;

public class SkipTask implements Task {

    private final JaggyBot bot;

    public SkipTask(JaggyBot bot) {
        this.bot = bot;
    }

    @Override
    public void execute() {
        bot.getPlayHandler().skip();
    }
}
