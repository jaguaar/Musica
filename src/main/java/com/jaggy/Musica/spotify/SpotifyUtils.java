package com.jaggy.Musica.spotify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpotifyUtils {
	private final SpotifyService spotifyService;

	@Autowired
	public SpotifyUtils(final SpotifyService spotifyService) {
		this.spotifyService = spotifyService;
	}

	public boolean isSpotifySong(final String url) {
		return url.toLowerCase().contains("open.spotify.com") && url.toLowerCase().contains("/track/");
	}

	public boolean isSpotifyPlaylist(final String url) {
		return url.toLowerCase().contains("open.spotify.com") && url.toLowerCase().contains("/playlist/");
	}

	public String getSpotifyIdentifier(final String url) {
		final int startIndex = url.lastIndexOf('/') + 1;
		final int endIndex = url.contains("?") ? url.indexOf('?') : url.length();

		return url.substring(startIndex, endIndex);
	}

	public String getSongTitle(final String song) {
		return spotifyService.getSongTitle(getSpotifyIdentifier(song));
	}

	public List<String> getSongTitles(final String playlistUrl) {
		return spotifyService.loadPlaylist(getSpotifyIdentifier(playlistUrl));
	}
}
