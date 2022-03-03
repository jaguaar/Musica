package com.jaggy.Musica.services.messaging;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = "true")
public record Compliment(String compliment) {
}
