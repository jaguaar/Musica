package com.jaggy.Musica.tasks.audio;

import com.jaggy.Musica.JaggyBot;
import com.jaggy.Musica.tasks.Task;

public class ClearTask implements Task {

    private final JaggyBot bot;

    public ClearTask(JaggyBot bot) {
        this.bot = bot;
    }

    @Override
    public void execute() {
        bot.getPlayHandler().clear();
    }
}
