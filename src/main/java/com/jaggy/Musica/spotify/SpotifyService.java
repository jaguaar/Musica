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

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

@Service
public class SpotifyService {

	private final Logger LOG = LogManager.getLogger(SpotifyService.class);

	private final SpotifyConnector spotifyConnector;

	@Autowired
	public SpotifyService(final SpotifyConnector spotifyConnector) {
		this.spotifyConnector = spotifyConnector;
	}

	public List<String> loadPlaylist(final String playlistId) throws IOException, ParseException, SpotifyWebApiException {
		final GetPlaylistRequest getPlaylistRequest = spotifyConnector.getApi().getPlaylist(playlistId).build();
		try {
			final Playlist playlist = getPlaylistRequest.execute();
			return Arrays.stream(playlist.getTracks().getItems())
					.map(track -> getSongTitle(track.getTrack().getId()))
					.collect(Collectors.toList());
		} catch (final IOException | SpotifyWebApiException | ParseException e) {
			LOG.error("Could not load playlist {} {}", playlistId, e);
			throw e;
		}
	}

	public List<String> loadAlbum(final String albumId) {
		final GetAlbumRequest albumRequest = spotifyConnector.getApi().getAlbum(albumId).build();
		try {
			final Album album = albumRequest.execute();
			return Arrays.stream(album.getTracks().getItems())
					.map(track -> getSongTitle(track.getId()))
					.collect(Collectors.toList());
		} catch (final IOException | SpotifyWebApiException | ParseException e) {
			LOG.error("Could not load album {} {}", albumId, e);
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
