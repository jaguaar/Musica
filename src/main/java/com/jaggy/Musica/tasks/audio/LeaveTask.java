package com.jaggy.Musica.tasks.audio;

import com.jaggy.Musica.JaggyBot;
import com.jaggy.Musica.tasks.Task;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class LeaveTask implements Task {

	private final JaggyBot bot;
	private final Message message;

	public LeaveTask(final JaggyBot bot, final Message message) {
		this.bot = bot;
		this.message = message;
	}

	@Override
	public void execute() {
		message.getChannel().sendMessage("I am out b*tches").queue();
		final VoiceChannel voiceChannel = message.getGuild().getSelfMember().getVoiceState().getChannel();
		if (voiceChannel != null) {
			message.getGuild().getAudioManager().closeAudioConnection();
		}
	}
}
