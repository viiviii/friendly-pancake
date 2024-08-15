package com.pancake.api;

import com.pancake.api.content.infra.api.TmdbClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public TmdbClient tmdbClient() {
        return memoryMovies();
    }

    private MemoryMovies memoryMovies() {
        return new MemoryMovies();
    }
}
