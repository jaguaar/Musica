package com.jaggy.Musica.youtube;

import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioTrack;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeSearchProvider;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sedmelluq.discord.lavaplayer.track.BasicAudioPlaylist;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class YoutubeUtils {
	private final YoutubeAudioSourceManager youtubeAudioSourceManager = new YoutubeAudioSourceManager();

	public boolean isYoutubeSong(String input) {
		return input.toLowerCase().contains("youtube.com");
	}

	public YoutubeAudioTrack getAudioTrack(final String song) {
		final String videoId = song.substring(song.indexOf("?v=") + 3);

		return (YoutubeAudioTrack) youtubeAudioSourceManager.loadTrackWithVideoId(videoId, true);
	}

	public AudioTrack searchSong(String song) {
		YoutubeSearchProvider youtubeSearchProvider = new YoutubeSearchProvider();
		final AudioItem audioItem = youtubeSearchProvider.loadSearchResult(song, new Function<AudioTrackInfo, AudioTrack>() {
			@Override
			public AudioTrack apply(final AudioTrackInfo audioTrackInfo) {
				return new YoutubeAudioTrack(audioTrackInfo, youtubeAudioSourceManager);
			}
		});

		return ((BasicAudioPlaylist) audioItem).getTracks().get(0);
	}
}
