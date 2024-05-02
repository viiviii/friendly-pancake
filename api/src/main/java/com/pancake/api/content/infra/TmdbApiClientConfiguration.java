package com.pancake.api.content.infra;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
class TmdbApiClientConfiguration {

    @Value("${api.tmdb.token}")
    private String token;
    
    @Bean
    RestClient tmdbClient(RestClient.Builder restClientBuilder) {
        // TODO: &language=ko 설정, snakeCase 한 곳에서
        return restClientBuilder
                .baseUrl("https://api.themoviedb.org/3")
                .defaultHeader(AUTHORIZATION, "Bearer " + token)
                .build();
    }
}
