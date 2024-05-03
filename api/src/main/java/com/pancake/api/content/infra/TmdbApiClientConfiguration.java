package com.pancake.api.content.infra;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
class TmdbApiClientConfiguration {

    @Value("${api.tmdb.token}")
    private String token;

    @Bean
    RestClient tmdbClient(RestClient.Builder restClientBuilder, Jackson2ObjectMapperBuilder mapper) {
        final var converter = new MappingJackson2HttpMessageConverter(mapper.propertyNamingStrategy(SNAKE_CASE).build());
        return restClientBuilder
                .baseUrl("https://api.themoviedb.org/3")
                .defaultHeader(AUTHORIZATION, "Bearer " + token)
                .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
                .messageConverters(converters -> converters.add(0, converter))
                .build();
    }
}
