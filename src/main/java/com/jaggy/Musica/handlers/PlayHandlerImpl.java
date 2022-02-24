package com.jaggy.Musica.handlers;

import com.jaggy.Musica.spotify.SpotifyUtils;
import com.jaggy.Musica.youtube.YoutubeUtils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Message;
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
	public void play(final Message message, final String url, boolean playNext) {
		final VoiceChannel channel = message.getMember().getVoiceState().getChannel();

		if (channel != null) {
			if (currentChannel != channel) {
				joinChannel(message, channel);
			}

			if (youtubeUtils.isYoutubeSong(url)) {
				playTrack(youtubeUtils.getAudioTrack(url), playNext);
				return;
			}

			if (spotifyUtils.isSpotifySong(url)) {
				final String songTitle = spotifyUtils.getSongTitle(url);
				playTrack(youtubeUtils.searchSong(songTitle), playNext);
				return;
			}

			if (spotifyUtils.isSpotifyPlaylist(url)) {
				final List<String> songTitles = spotifyUtils.getSongTitles(url);
				songTitles.forEach(title -> playTrack(youtubeUtils.searchSong(title), playNext));
				return;
			}

			//Just search it
			playTrack(youtubeUtils.searchSong(url), playNext);
		}
	}

	@Override
	public void playTrack(final AudioTrack audioTrack, final boolean playNext) {
		soundHandler.getTrackScheduler().queue(audioTrack, playNext);
	}

	private void joinChannel(final Message message, final VoiceChannel channel) {
		currentChannel = channel;

		AudioManager manager = message.getGuild().getAudioManager();
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
}
