package com.jaggy.Musica.tasks.messaging;

import com.jaggy.Musica.JaggyBot;
import com.jaggy.Musica.tasks.Task;

import net.dv8tion.jda.api.entities.Message;

public class InsultTask implements Task {

	private final JaggyBot bot;
	private final Message message;

	public InsultTask(final JaggyBot bot, final Message message) {
		this.bot = bot;
		this.message = message;
	}

	@Override
	public void execute() {
		final String insult = bot.getInsultService().generateInsult();
		message.getMentionedMembers().forEach(member -> {
			member.getUser().openPrivateChannel().queue(privateChannel -> {
				privateChannel.sendMessage(insult).queue();
			});
		});

	}
}
