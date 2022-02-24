package com.jaggy.Musica.tasks;

import com.jaggy.Musica.JaggyBot;
import com.jaggy.Musica.events.CommandEvent;
import com.jaggy.Musica.tasks.audio.PlayTask;
import com.jaggy.Musica.tasks.audio.QueueTask;
import com.jaggy.Musica.tasks.audio.SkipTask;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class TaskFactoryImpl implements TaskFactory {

    private final JaggyBot bot;

    public TaskFactoryImpl(@Lazy JaggyBot bot) {
        this.bot = bot;
    }

    @Override
    public Task buildTask(CommandEvent commandEvent) {
        return switch(commandEvent.getAction()){
            case "play" -> new PlayTask(bot, commandEvent.getMessage(), commandEvent.getArgs().get(0), false);
            case "playnext" -> new PlayTask(bot, commandEvent.getMessage(), commandEvent.getArgs().get(0), true);
            case "skip" -> new SkipTask(bot);
            case "queue" -> new QueueTask(bot, commandEvent.getMessage());
            default -> new UnknownTask(commandEvent.getMessage());
        };
    }
}
