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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;

@Service
public class SpotifyService {

	private final Logger LOG = LogManager.getLogger(SpotifyService.class);

	private final SpotifyApi spotifyApi;
	private final ClientCredentialsRequest clientCredentialsRequest;
	private final ClientCredentials clientCredentials;

	@Autowired
	public SpotifyService(@Value("${spotify.client.id}") final String clientId,
			@Value("${spotify.client.secret}") final String clientSecret) throws IOException, ParseException, SpotifyWebApiException {
		spotifyApi = new SpotifyApi.Builder()
				.setClientId(clientId)
				.setClientSecret(clientSecret)
				.build();

		clientCredentialsRequest = spotifyApi.clientCredentials()
				.build();

		clientCredentials = clientCredentialsRequest.execute();

		spotifyApi.setAccessToken(clientCredentials.getAccessToken());
	}

	public List<String> loadPlaylist(final String playlistId) {
		final GetPlaylistRequest getPlaylistRequest = spotifyApi.getPlaylist(playlistId).build();
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
		final GetTrackRequest getTrackRequest = spotifyApi.getTrack(trackId).build();
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
