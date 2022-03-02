package com.jaggy.Musica.handlers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jaggy.Musica.spotify.SpotifyUtils;
import com.jaggy.Musica.youtube.YoutubeUtils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

@Component
public class PlayHandlerImpl implements PlayHandler {
	private final SoundHandler soundHandler;
	private final YoutubeUtils youtubeUtils;
	private final SpotifyUtils spotifyUtils;

	private VoiceChannel currentChannel;

	@Autowired
	public PlayHandlerImpl(final SoundHandler soundHandler, final YoutubeUtils youtubeUtils, final SpotifyUtils spotifyUtils) {
		this.soundHandler = soundHandler;
		this.youtubeUtils = youtubeUtils;
		this.spotifyUtils = spotifyUtils;
	}

	@Override
	public void play(final Message message, final List<String> args, final boolean playNext, boolean shuffle) {
		final VoiceChannel channel = message.getMember().getVoiceState().getChannel();

		if (channel != null) {
			if (currentChannel != channel) {
				joinChannel(message, channel);
			}

			final String url = args.get(0);

			if (spotifyUtils.isSpotifyPlaylist(url)) {
				final List<String> songTitles = spotifyUtils.getSongTitles(url);

				if(shuffle) {
					Collections.shuffle(songTitles);
				}

				songTitles.parallelStream().map(youtubeUtils::searchSong).forEachOrdered(track -> playTrack(track, playNext));

				final String messageText =
						(shuffle ? ":twisted_rightwards_arrows: Shuffled " : ":arrow_forward: Added ") +
						songTitles.size() +
						" songs from the playlist " +
						(shuffle ? "into" : "to") +
						" the Queue!";

				message.getChannel().sendMessage(messageText).queue();
				return;
			}

			final AudioTrack audioTrack;

			if (youtubeUtils.isYoutubeSong(url)) {
				audioTrack = youtubeUtils.getAudioTrack(url);
			} else if (spotifyUtils.isSpotifySong(url)) {
				final String songTitle = spotifyUtils.getSongTitle(url);
				audioTrack = youtubeUtils.searchSong(songTitle);
			} else {
				// Just search it
				audioTrack = youtubeUtils.searchSong(args.stream().collect(Collectors.joining(" ")));
			}

			message.getChannel()
					.sendMessage(":arrow_forward: Added " + audioTrack.getInfo().title + " to the Queue! (" + audioTrack.getInfo().uri + ")").queue();
			playTrack(audioTrack, playNext);
		}
	}

	@Override
	public void playTrack(final AudioTrack audioTrack, final boolean playNext) {
		soundHandler.getTrackScheduler().queue(audioTrack, playNext);
	}

	private void joinChannel(final Message message, final VoiceChannel channel) {
		currentChannel = channel;

		final AudioManager manager = message.getGuild().getAudioManager();
		manager.setSendingHandler(soundHandler);
		manager.openAudioConnection(channel);
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
