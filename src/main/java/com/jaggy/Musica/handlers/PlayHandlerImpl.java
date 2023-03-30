package com.jaggy.Musica.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jaggy.Musica.spotify.SpotifyUtils;
import com.jaggy.Musica.youtube.YoutubeUtils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.managers.AudioManager;

@Component
public class PlayHandlerImpl implements PlayHandler {
	public static final String ADDED = ":arrow_forward: Added ";
	private final SoundHandler soundHandler;
	private final YoutubeUtils youtubeUtils;
	private final SpotifyUtils spotifyUtils;

	@Autowired
	public PlayHandlerImpl(final SoundHandler soundHandler, final YoutubeUtils youtubeUtils, final SpotifyUtils spotifyUtils) {
		this.soundHandler = soundHandler;
		this.youtubeUtils = youtubeUtils;
		this.spotifyUtils = spotifyUtils;
	}

	@Override
	public void play(final Message message, final List<String> args, final boolean playNext, final boolean shuffle) {
		if (withActiveConnection(message)) {

			final String url = args.get(0);
			final List<AudioTrack> tracks = new ArrayList<>();

			if (spotifyUtils.isSpotify(url)) {
				message.getChannel().sendMessage("Processing " + url).queue();
				final List<String> songTitles = spotifyUtils.getSongTitles(url);
				songTitles.parallelStream().map(youtubeUtils::searchSong).forEachOrdered(tracks::add);
			} else if (youtubeUtils.isYoutubeSong(url)) {
				tracks.add(youtubeUtils.getAudioTrack(url));
			} else {
				// Just search it
				tracks.add(youtubeUtils.searchSong(args.stream().collect(Collectors.joining(" "))));
			}

			if (shuffle) {
				Collections.shuffle(tracks);
			}

			tracks.forEach(track -> {
				playTrack(track, playNext);
			});

			if (tracks.size() == 1) {
				message.getChannel().sendMessage(ADDED + tracks.get(0).getInfo().title + " to the Queue! (" + tracks.get(0).getInfo().uri + ")")
						.queue();
			} else if (tracks.size() > 1) {
				final String messageText = ADDED + tracks.size() + " songs to the Queue!";
				message.getChannel().sendMessage(shuffle ? messageText + " (Shuffled!)" : messageText).queue();
			}
		}
	}

	@Override
	public void playTrack(final AudioTrack audioTrack, final boolean playNext) {
		soundHandler.getTrackScheduler().queue(audioTrack, playNext);
	}

	private boolean withActiveConnection(final Message message) {
		final var channel = message.getMember().getVoiceState().getChannel();
		if (channel == null) {
			return false;
		}

		if (message.getGuild().getAudioManager().getConnectedChannel() != channel) {
			final AudioManager manager = message.getGuild().getAudioManager();
			manager.setSendingHandler(soundHandler);
			manager.openAudioConnection(channel);
		}
		return true;
	}

	@Override
	public List<AudioTrack> getQueue() {
		return soundHandler.getTrackScheduler().getQueue();
	}

	@Override
	public AudioTrack getCurrentlyPlaying() {
		return soundHandler.getTrackScheduler().getCurrentlyPlaying();
	}

	@Override
	public AudioTrack skip() {
		final AudioTrack currentlyPlaying = soundHandler.getTrackScheduler().getCurrentlyPlaying();
		soundHandler.getTrackScheduler().skip();
		return currentlyPlaying;
	}

	@Override
	public void clear() {
		soundHandler.getTrackScheduler().clear();
	}

	@Override
	public void shuffle() {
		soundHandler.getTrackScheduler().shuffle();
	}
}
