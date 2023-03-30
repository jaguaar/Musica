package com.jaggy.Musica.tasks;

import net.dv8tion.jda.api.entities.Message;

public class UnknownTask implements Task {

	private final Message message;

	public UnknownTask(final Message message) {
		this.message = message;
	}

	@Override
	public void execute() {
		final var channel = message.getChannel();
		channel.sendMessage("Unknown command dimwit").queue();
	}
}
