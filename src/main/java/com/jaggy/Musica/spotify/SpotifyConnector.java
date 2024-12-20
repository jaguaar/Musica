package com.jaggy.Musica.spotify;

import org.apache.hc.core5.http.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class SpotifyConnector {

	private final Logger LOG = LogManager.getLogger(SpotifyConnector.class);

	private final SpotifyApi spotifyApi;
	private ClientCredentials clientCredentials;

	private LocalDateTime tokenDate = LocalDateTime.now().minus(Duration.ofMinutes(60));

	public SpotifyConnector(@Value("${spotify.client.id}") final String clientId, @Value("${spotify.client.secret}") final String clientSecret)
			throws IOException, ParseException, SpotifyWebApiException {
		spotifyApi = new SpotifyApi.Builder()
				.setClientId(clientId)
				.setClientSecret(clientSecret)
				.build();
		refreshToken();
	}

	public SpotifyApi getApi() {
		refreshToken();
		return spotifyApi;
	}

	private void refreshToken() {
		final LocalDateTime now = LocalDateTime.now();
		if (now.minus(Duration.ofMinutes(50)).isAfter(tokenDate)) {
			LOG.info("Fetching new spotify token");
			try {
				clientCredentials = spotifyApi.clientCredentials().build().execute();
				spotifyApi.setAccessToken(clientCredentials.getAccessToken());
				tokenDate = now;
			} catch (final IOException | SpotifyWebApiException | ParseException e) {
				LOG.error("Problem Refreshing Token", e);
			}
		}
	}
}
