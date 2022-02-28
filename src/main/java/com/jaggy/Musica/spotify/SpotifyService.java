package com.jaggy.Musica.spotify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.hc.core5.http.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;

@Service
public class SpotifyService {

	private final Logger LOG = LogManager.getLogger(SpotifyService.class);

	private final SpotifyConnector spotifyConnector;

	@Autowired
	public SpotifyService(final SpotifyConnector spotifyConnector) {
		this.spotifyConnector = spotifyConnector;
	}

	public List<String> loadPlaylist(final String playlistId) {
		final GetPlaylistRequest getPlaylistRequest = spotifyConnector.getApi().getPlaylist(playlistId).build();
		try {
			final Playlist playlist = getPlaylistRequest.execute();
			return Arrays.stream(playlist.getTracks().getItems())
					.map(track -> getSongTitle(track.getTrack().getId()))
					.collect(Collectors.toList());
		} catch (final IOException | SpotifyWebApiException | ParseException e) {
			LOG.error("Could not load playlist {} {}", playlistId, e);
		}

		return new ArrayList<>();
	}

	public String getSongTitle(final String trackId) {
		final GetTrackRequest getTrackRequest = spotifyConnector.getApi().getTrack(trackId).build();
		try {
			final Track actualTrack = getTrackRequest.execute();
			final List<String> artists = Arrays.stream(actualTrack.getArtists()).map(ArtistSimplified::getName).collect(Collectors.toList());
			return actualTrack.getName() + " - " + String.join(" ", artists);
		} catch (final IOException | SpotifyWebApiException | ParseException e) {
			LOG.error("Could not load song {}", trackId, e);
		}

		return null;
	}
}
