package com.jaggy.Musica.tasks;

import com.jaggy.Musica.JaggyBot;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class UnknownTask implements Task {

    private final Message message;

    public UnknownTask(Message message) {
        this.message = message;
    }

    @Override
    public void execute() {
        final MessageChannel channel = message.getChannel();
        channel.sendMessage("Unknown command dimwit");
    }
}
