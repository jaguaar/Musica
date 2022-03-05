package com.jaggy.Musica.tasks.messaging;

import java.util.function.Supplier;

import com.jaggy.Musica.JaggyBot;
import com.jaggy.Musica.tasks.Task;

import net.dv8tion.jda.api.entities.Message;

abstract class AbstractPrivateMessageTask implements Task {

	private final JaggyBot bot;
	private final Message message;

	public AbstractPrivateMessageTask(final JaggyBot bot, final Message message) {
		this.bot = bot;
		this.message = message;
	}

	@Override
	public void execute() {
		message.getMentionedMembers().forEach(member -> {
			member.getUser().openPrivateChannel().queue(privateChannel -> {
				privateChannel.sendMessage(getMessage().get()).queue();
			});
		});
	}

	public JaggyBot getBot() {
		return bot;
	}

	abstract Supplier<String> getMessage();
}
