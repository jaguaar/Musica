package com.jaggy.Musica.spotify;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import org.apache.hc.core5.http.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;

@Component
public class SpotifyConnector {

	private final Logger LOG = LogManager.getLogger(SpotifyConnector.class);

	private final SpotifyApi spotifyApi;
	private final ClientCredentials clientCredentials;

	private LocalDateTime tokenDate = LocalDateTime.now().minus(Duration.ofMinutes(60));

	public SpotifyConnector(@Value("${spotify.client.id}") final String clientId, @Value("${spotify.client.secret}") final String clientSecret)
			throws IOException, ParseException, SpotifyWebApiException {
		spotifyApi = new com.wrapper.spotify.SpotifyApi.Builder()
				.setClientId(clientId)
				.setClientSecret(clientSecret)
				.build();
		try {
			clientCredentials = spotifyApi.clientCredentials().build().execute();
			refreshToken();
		} catch (final IOException | ParseException | SpotifyWebApiException e) {
			LOG.error("Problem setting up SpotifyConnector", e);
			throw e;
		}
	}

	public SpotifyApi getApi() {
		refreshToken();
		return spotifyApi;
	}

	private void refreshToken() {
		final LocalDateTime now = LocalDateTime.now();
		if (now.minus(Duration.ofMinutes(50)).isAfter(tokenDate)) {
			LOG.info("Fetching new spotify token");
			spotifyApi.setAccessToken(clientCredentials.getAccessToken());
			tokenDate = now;
		}
	}
}
