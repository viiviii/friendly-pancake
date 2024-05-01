package com.pancake.api.content.infra;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
class TmdbApiClientConfiguration {

    @Bean
    RestClient tmdbClient(RestClient.Builder restClientBuilder) {
        // TODO: API KEY 설정, &language=ko 설정, snakeCase 한 곳에서
        return restClientBuilder
                .baseUrl("https://api.themoviedb.org/3")
                .build();
    }
}
