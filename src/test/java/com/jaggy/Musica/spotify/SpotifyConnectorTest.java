package com.jaggy.Musica.spotify;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class SpotifyConnectorTest {

	@Test
	void imBadAtLocalDateTime() {

		final LocalDateTime expiredToke = LocalDateTime.now().minus(Duration.ofMinutes(70));
		final LocalDateTime now = LocalDateTime.now();

		assertThat(now.minus(Duration.ofMinutes(50)).isAfter(expiredToke)).isEqualTo(true);

	}

}