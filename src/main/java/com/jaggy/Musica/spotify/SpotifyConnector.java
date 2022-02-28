package com.jaggy.Musica.spotify;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

@Component
public class SpotifyConnector {

	private final SpotifyApi spotifyApi;
	private final ClientCredentialsRequest clientCredentialsRequest;

	private LocalDateTime tokenDate = LocalDateTime.now();

	public SpotifyConnector(@Value("${spotify.client.id}") final String clientId, @Value("${spotify.client.secret}") final String clientSecret)
			throws IOException, ParseException, SpotifyWebApiException {
		spotifyApi = new com.wrapper.spotify.SpotifyApi.Builder()
				.setClientId(clientId)
				.setClientSecret(clientSecret)
				.build();

		clientCredentialsRequest = spotifyApi.clientCredentials()
				.build();

		final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

		spotifyApi.setAccessToken(clientCredentials.getAccessToken());
	}

	public SpotifyApi getApi() {
		refreshToken();
		return spotifyApi;
	}

	private void refreshToken() {
		final LocalDateTime now = LocalDateTime.now();
		if (now.minus(Duration.ofMinutes(50)).isAfter(tokenDate)) {
			spotifyApi.setAccessToken(spotifyApi.getRefreshToken());
			tokenDate = now;
		}
	}
}
