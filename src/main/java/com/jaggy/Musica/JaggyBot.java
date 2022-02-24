package com.jaggy.Musica;

import java.util.List;

import javax.security.auth.login.LoginException;

import com.jaggy.Musica.events.CommandEvent;
import com.jaggy.Musica.events.CommandEventListener;
import com.jaggy.Musica.events.MessageEventListener;
import com.jaggy.Musica.handlers.PlayHandler;
import com.jaggy.Musica.tasks.Task;
import com.jaggy.Musica.tasks.TaskFactory;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JaggyBot implements CommandEventListener {

    @Value("discord.token")
    private String token;

    private final JDA jda;

    private final TaskFactory taskFactory;
    private final PlayHandler playHandler;

    public JaggyBot(MessageEventListener messageEventListener, TaskFactory taskFactory, PlayHandler playHandler) throws LoginException {
        this.jda = JDABuilder
                .createDefault(token, List.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES))
                .addEventListeners(messageEventListener)
                .build();
        this.taskFactory = taskFactory;
        this.playHandler = playHandler;
    }

    @Override
    public void onCommandEvent(CommandEvent commandEvent) {
        final Task task = taskFactory.buildTask(commandEvent);
        task.execute();
    }

    public PlayHandler getPlayHandler() {
        return playHandler;
    }

    public JDA getJda() {
        return jda;
    }

}
