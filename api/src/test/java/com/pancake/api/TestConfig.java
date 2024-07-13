package com.pancake.api;

import com.pancake.api.search.FindContentMetadata;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public FindContentMetadata findContentMetadata() {
        return new MemoryMetadataRepository();
    }
}
