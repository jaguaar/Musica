package com.jaggy.Musica.tasks.messaging;

import java.util.function.Supplier;

import com.jaggy.Musica.JaggyBot;

import net.dv8tion.jda.api.entities.Message;

public class InsultTask extends AbstractPrivateMessageTask {

	public InsultTask(final JaggyBot bot, final Message message) {
		super(bot, message);
	}

	@Override
	Supplier<String> getMessage() {
		return () -> getBot().getMessageGenerator().generateInsult();
	}
}
