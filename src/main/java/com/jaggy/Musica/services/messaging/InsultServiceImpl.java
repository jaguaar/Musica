package com.jaggy.Musica.services.messaging;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class InsultServiceImpl implements InsultService {

	private final RestTemplate restTemplate;

	public InsultServiceImpl(final RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@Override
	public String generateInsult() {
		final Insult response = restTemplate.getForObject("https://evilinsult.com/generate_insult.php?lang=en&type=json", Insult.class);
		return response.insult();
	}

}
