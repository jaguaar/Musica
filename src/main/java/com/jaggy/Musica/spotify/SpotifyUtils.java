package com.jaggy.Musica.spotify;

import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SpotifyUtils {

	public static final String OPEN_SPOTIFY_COM = "open.spotify.com";
	private final SpotifyService spotifyService;

	@Autowired
	public SpotifyUtils(final SpotifyService spotifyService) {
		this.spotifyService = spotifyService;
	}

	public boolean isSpotify(final String url) {
		return url.toLowerCase().contains(OPEN_SPOTIFY_COM);
	}

	public List<String> getSongTitles(final String url) throws IOException, ParseException, SpotifyWebApiException {
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

	private List<String> getPlayListSongTitles(final String url) throws IOException, ParseException, SpotifyWebApiException {
		return spotifyService.loadPlaylist(getSpotifyIdentifier(url));
	}

	private List<String> getAlbumSongTitles(final String url) {
		return spotifyService.loadAlbum(getSpotifyIdentifier(url));
	}

	private boolean isSpotifySong(final String url) {
		return url.toLowerCase().contains(OPEN_SPOTIFY_COM) && url.toLowerCase().contains("/track/");
	}

	private boolean isSpotifyPlaylist(final String url) {
		return url.toLowerCase().contains(OPEN_SPOTIFY_COM) && url.toLowerCase().contains("/playlist/");
	}

	private boolean isSpotifyAlbum(final String url) {
		return url.toLowerCase().contains(OPEN_SPOTIFY_COM) && url.toLowerCase().contains("/album/");
	}

}
