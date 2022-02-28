package com.jaggy.Musica.tasks;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.jaggy.Musica.JaggyBot;
import com.jaggy.Musica.events.CommandEvent;
import com.jaggy.Musica.tasks.audio.ClearTask;
import com.jaggy.Musica.tasks.audio.PlayTask;
import com.jaggy.Musica.tasks.audio.QueueTask;
import com.jaggy.Musica.tasks.audio.ShuffleTask;
import com.jaggy.Musica.tasks.audio.SkipTask;
import com.jaggy.Musica.tasks.messaging.InsultTask;

@Component
public class TaskFactoryImpl implements TaskFactory {

	private final JaggyBot bot;

	public TaskFactoryImpl(@Lazy final JaggyBot bot) {
		this.bot = bot;
	}

	@Override
	public Task buildTask(final CommandEvent commandEvent) {
		return switch (commandEvent.getAction()) {
		case "play", "p" -> new PlayTask(bot, commandEvent.getMessage(), commandEvent.getArgs(), false);
		case "playnext", "pn" -> new PlayTask(bot, commandEvent.getMessage(), commandEvent.getArgs(), true);
		case "skip", "s" -> new SkipTask(bot, commandEvent.getMessage());
		case "queue", "q" -> new QueueTask(bot, commandEvent.getMessage());
		case "shuffle" -> new ShuffleTask(bot, commandEvent.getMessage());
		case "clear" -> new ClearTask(bot, commandEvent.getMessage());
		case "bravo" -> new InsultTask(bot, commandEvent.getMessage());
		default -> new UnknownTask(commandEvent.getMessage());
		};
	}
}
