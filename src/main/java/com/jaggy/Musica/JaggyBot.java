package com.jaggy.Musica;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jaggy.Musica.events.CommandEvent;
import com.jaggy.Musica.events.CommandEventListener;
import com.jaggy.Musica.events.MessageEventListener;
import com.jaggy.Musica.handlers.PlayHandler;
import com.jaggy.Musica.services.messaging.InsultService;
import com.jaggy.Musica.tasks.Task;
import com.jaggy.Musica.tasks.TaskFactory;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

@Component
public class JaggyBot implements CommandEventListener {
	private final JDA jda;

	private final TaskFactory taskFactory;
	private final PlayHandler playHandler;
	private final InsultService insultService;

	public JaggyBot(final MessageEventListener messageEventListener, final TaskFactory taskFactory, final PlayHandler playHandler,
                    @Value("${discord.token}") final String token, final InsultService insultService) throws LoginException {
		this.jda = JDABuilder
				.createDefault(token)
				.addEventListeners(messageEventListener)
				.build();
		this.taskFactory = taskFactory;
		this.playHandler = playHandler;
		this.insultService = insultService;
	}

	@Override
	public void onCommandEvent(final CommandEvent commandEvent) {
		final Task task = taskFactory.buildTask(commandEvent);
		task.execute();
	}

	public PlayHandler getPlayHandler() {
		return playHandler;
	}

	public JDA getJda() {
		return jda;
	}

	public InsultService getInsultService() {
		return insultService;
	}
}
