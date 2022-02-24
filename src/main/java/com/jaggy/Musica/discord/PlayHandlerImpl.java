package com.jaggy.Musica.discord;

import com.jaggy.Musica.spotify.SpotifyUtils;
import com.jaggy.Musica.youtube.YoutubeUtils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
	public void play(final GuildMessageReceivedEvent event, final String url, boolean playNext) {
		if (event.getAuthor().isBot()) {
			return;
		}

		final VoiceChannel channel = event.getMember().getVoiceState().getChannel();

		if (channel != null) {
			if (currentChannel != channel) {
				joinChannel(event, channel);
			}

			if (spotifyUtils.isSpotifyPlaylist(url)) {
				final List<String> songTitles = spotifyUtils.getSongTitles(url);
				songTitles.forEach(title -> playTrack(youtubeUtils.searchSong(title), playNext));
				return;
			}

			final AudioTrack audioTrack;

			if (youtubeUtils.isYoutubeSong(url)) {
				audioTrack = youtubeUtils.getAudioTrack(url);
			} else if (spotifyUtils.isSpotifySong(url)) {
				final String songTitle = spotifyUtils.getSongTitle(url);
				audioTrack = youtubeUtils.searchSong(songTitle);
			} else {
				//Just search it
				audioTrack = youtubeUtils.searchSong(url);
			}

			MessageHandler.sendMessage("Added " + url + " to the Queue!", event);
			playTrack(audioTrack, playNext);
		}
	}

	@Override
	public void playTrack(final AudioTrack audioTrack, final boolean playNext) {
		soundHandler.getTrackScheduler().queue(audioTrack, playNext);
	}

	private void joinChannel(final GuildMessageReceivedEvent event, final VoiceChannel channel) {
		currentChannel = channel;

		System.out.println("Joining channel " + channel.getName());

		AudioManager manager = event.getGuild().getAudioManager();
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
	public void skip() {
		soundHandler.getTrackScheduler().skip();
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
