package com.jaggy.Musica.spotify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpotifyUtils {
	private final SpotifyService spotifyService;

	@Autowired
	public SpotifyUtils(final SpotifyService spotifyService) {
		this.spotifyService = spotifyService;
	}

	public boolean isSpotify(final String url) {
		return url.toLowerCase().contains("open.spotify.com");
	}

	public List<String> getSongTitles(final String url) {
		final List<String> songTitles = new ArrayList<>();

		if (isSpotifySong(url)) {
			songTitles.add(getSongTitle(url));
		} else if (isSpotifyPlaylist(url)) {
			songTitles.addAll(getPlayListSongTitles(url));
		} else if (isSpotifyAlbum(url)) {
			songTitles.addAll(getAlbumSongTitles(url));
		}

		return songTitles;
	}

	private String getSpotifyIdentifier(final String url) {
		final int startIndex = url.lastIndexOf('/') + 1;
		final int endIndex = url.contains("?") ? url.indexOf('?') : url.length();

		return url.substring(startIndex, endIndex);
	}

	private String getSongTitle(final String url) {
		return spotifyService.getSongTitle(getSpotifyIdentifier(url));
	}

	private List<String> getPlayListSongTitles(final String url) {
		return spotifyService.loadPlaylist(getSpotifyIdentifier(url));
	}

	private List<String> getAlbumSongTitles(final String url) {
		return spotifyService.loadAlbum(getSpotifyIdentifier(url));
	}

	private boolean isSpotifySong(final String url) {
		return url.toLowerCase().contains("open.spotify.com") && url.toLowerCase().contains("/track/");
	}

	private boolean isSpotifyPlaylist(final String url) {
		return url.toLowerCase().contains("open.spotify.com") && url.toLowerCase().contains("/playlist/");
	}

	private boolean isSpotifyAlbum(final String url) {
		return url.toLowerCase().contains("open.spotify.com") && url.toLowerCase().contains("/album/");
	}

}
