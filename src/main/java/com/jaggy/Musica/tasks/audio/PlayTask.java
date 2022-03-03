package com.jaggy.Musica.tasks.audio;

import java.util.List;

import com.jaggy.Musica.JaggyBot;
import com.jaggy.Musica.tasks.Task;

import net.dv8tion.jda.api.entities.Message;

public class PlayTask implements Task {

	private final JaggyBot bot;

	private final Message message;
	private final List<String> args;
	private final boolean next;
	private final boolean shuffle;

	public PlayTask(final JaggyBot bot, final Message message, final List<String> args, final boolean next, final boolean shuffle) {
		this.bot = bot;
		this.message = message;
		this.args = args;
		this.next = next;
		this.shuffle = shuffle;
	}

	@Override
	public void execute() {
		bot.getPlayHandler().play(message, args, next, shuffle);
	}
}
