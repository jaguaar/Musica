package com.jaggy.Musica.services.messaging;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MessageGeneratorImpl implements MessageGenerator {

	private final RestTemplate restTemplate;

	public MessageGeneratorImpl(final RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@Override
	public String generateInsult() {
		final Insult response = restTemplate.getForObject("https://evilinsult.com/generate_insult.php?lang=en&type=json", Insult.class);
		return response.insult();
	}

	@Override
	public String generateCompliment() {
		final Compliment response = restTemplate.getForObject("https://complimentr.com/api", Compliment.class);
		return response.compliment();
	}
}
