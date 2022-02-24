package com.jaggy.Musica.tasks.audio;

import com.jaggy.Musica.JaggyBot;
import com.jaggy.Musica.tasks.Task;
import net.dv8tion.jda.api.entities.Message;

public class ClearTask implements Task {

    private final JaggyBot bot;
    private final Message message;

    public ClearTask(JaggyBot bot, Message message) {
        this.bot = bot;
        this.message = message;
    }

    @Override
    public void execute() {
        bot.getPlayHandler().clear();
        message.getChannel().sendMessage(":broom: Cleared queue.").queue();
    }
}
