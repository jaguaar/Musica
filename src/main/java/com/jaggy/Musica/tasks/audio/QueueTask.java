package com.jaggy.Musica.tasks.audio;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jaggy.Musica.JaggyBot;
import com.jaggy.Musica.handlers.PlayHandler;
import com.jaggy.Musica.tasks.Task;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class QueueTask implements Task {

	private final JaggyBot bot;

	private final Message message;
	private final int MAX_TEXT_LENGTH = 1950;

	public QueueTask(final JaggyBot bot, final Message message) {
		this.bot = bot;
		this.message = message;
	}

	@Override
	public void execute() {
		final MessageChannelUnion channel = message.getChannel();
		final PlayHandler playHandler = bot.getPlayHandler();

		final AudioTrack currentlyPlaying = playHandler.getCurrentlyPlaying();

		if (currentlyPlaying == null) {
			channel.sendMessage("Nothing's playing right now!").queue();
			return;
		}

		final List<AudioTrack> queue = playHandler.getQueue();
		final StringBuilder sb = new StringBuilder(":arrow_forward: ");
		sb.append(String.format("%s (%s / %s)", currentlyPlaying.getInfo().title,
				formatMillis(currentlyPlaying.getPosition()), formatMillis(currentlyPlaying.getDuration())));

		if (!queue.isEmpty()) {
			boolean overflowingTextLimit = false;
			int count = 1;

			sb.append("\n:notes: Current Queue (Song count: ").append(queue.size()).append("):\n");
			for (final AudioTrack audioTrack : queue) {
				if (overflowingTextLimit) {
					continue;
				}

				final StringBuilder currentTrackText = new StringBuilder();

				if (count == 1) {
					currentTrackText.append("```");
				} else {
					currentTrackText.append("\n");
				}

				currentTrackText.append(count);
				currentTrackText.append(". ");
				currentTrackText.append(String.format("%s (%s)", audioTrack.getInfo().title, formatMillis(audioTrack.getDuration())));

				if (!((sb.length() + currentTrackText.length()) > MAX_TEXT_LENGTH)) {
					sb.append(currentTrackText);
				} else {
					overflowingTextLimit = true;
				}

				count++;
			}

			if (overflowingTextLimit) {
				sb.append("\n... And more...");
			}

			sb.append("```");
		}

		channel.sendMessage(sb.toString()).queue();
	}

	private String formatMillis(final long ms) {
		if (ms < 60 * 60 * 1000) {
			return String.format("%02d:%02d",
					TimeUnit.MILLISECONDS.toMinutes(ms),
					TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms)));
		} else {
			return String.format("%02d:%02d:%02d",
					TimeUnit.MILLISECONDS.toHours(ms),
					TimeUnit.MILLISECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ms)),
					TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms)));
		}
	}
}
