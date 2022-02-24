package com.jaggy.Musica.discord;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Async
public class MessageHandlerImpl implements MessageHandler {
	private final PlayHandler playHandler;
	private final MessageUtils messageUtils;

	@Value("${command.prefix}")
	private String PREFIX;

	@Autowired
	public MessageHandlerImpl(final PlayHandler playHandler, final MessageUtils messageUtils) {
		this.playHandler = playHandler;
		this.messageUtils = messageUtils;
	}

	@Override
	public void handleMessage(final GenericEvent event) {
		final String message = ((GuildMessageReceivedEvent) event).getMessage().getContentDisplay();

		if (message.startsWith(PREFIX)) {
			final String command = messageUtils.getCommand(message);

			if (command.equals("play") || command.equals("p")) {
				final String url = messageUtils.getUrl(message);
				playHandler.play((GuildMessageReceivedEvent) event, url, false);
			}

			if (command.equals("playnext") || command.equals("pn")) {
				final String url = messageUtils.getUrl(message);
				playHandler.play((GuildMessageReceivedEvent) event, url, true);
			}

			if (command.equals("skip")) {
				playHandler.skip();
				MessageHandler.sendMessage(":arrow_forward: Skipped song.", (GuildMessageReceivedEvent) event);
			}

			if (command.equals("clear")) {
				playHandler.clear();
				MessageHandler.sendMessage(":stop_button: The player has stopped and the queue has been cleared.", (GuildMessageReceivedEvent) event);
			}

			if (command.equals("queue")) {
				handleQueueCommand((GuildMessageReceivedEvent) event);
			}

			if (command.equals("shuffle")) {
				playHandler.shuffle();
				MessageHandler.sendMessage(":twisted_rightwards_arrows: Shuffled Queue.", (GuildMessageReceivedEvent) event);
			}
		}
	}

	private void handleQueueCommand(final GuildMessageReceivedEvent event) {
		MessageChannel channel = event.getChannel();

		AudioTrack currentlyPlaying = playHandler.getCurrentlyPlaying();

		if (currentlyPlaying == null) {
			channel.sendMessage("Nothing's playing right now!").queue();
			return;
		}

		final List<AudioTrack> queue = playHandler.getQueue();
		StringBuilder sb = new StringBuilder(":arrow_forward: ");
		sb.append(currentlyPlaying.getInfo().title);

		if (!queue.isEmpty()) {
			boolean overflowingTextLimit = false;
			int count = 1;

			sb.append("\n:notes: Current Queue:\n`");
			for (final AudioTrack audioTrack : queue) {
				StringBuilder currentTrackText = new StringBuilder("\n");
				currentTrackText.append(count);
				currentTrackText.append(". ");
				currentTrackText.append(audioTrack.getInfo().title);

				if (!((sb.length() + currentTrackText.length()) > 1950)) {
					sb.append(currentTrackText);
				} else {
					overflowingTextLimit = true;
				}

				count++;
			}

			if (overflowingTextLimit) {
				sb.append("\n... And more...");
			}

			sb.append("`");
		}

		channel.sendMessage(sb.toString()).queue();
	}
}
