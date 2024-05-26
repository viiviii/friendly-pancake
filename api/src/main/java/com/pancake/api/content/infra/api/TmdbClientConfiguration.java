package com.pancake.api.content.infra.api;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInitializer;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.net.URI;
import java.util.List;
import java.util.function.Consumer;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

@Configuration(proxyBeanMethods = false)
class TmdbClientConfiguration {

    // TODO: error status code 처리
    @Bean
    TmdbClient tmdbApiClient(RestClient.Builder client, @Value("${api.tmdb.token}") String token, Jackson2ObjectMapperBuilder objectMapper) {
        return createFrom(client
                .requestInitializer(setRequiredHeadersWith(token))
                .requestInterceptor(addLanguageQuery())
                .messageConverters(addSnakeCaseConverter(objectMapper))
        );
    }

    private TmdbClient createFrom(RestClient.Builder client) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(client.build()))
                .build()
                .createClient(TmdbClient.class);
    }

    private ClientHttpRequestInitializer setRequiredHeadersWith(String token) {
        return request -> {
            final var headers = request.getHeaders();
            headers.setBearerAuth(token);
            headers.setAccept(List.of(APPLICATION_JSON));
        };
    }

    private ClientHttpRequestInterceptor addLanguageQuery() {
        return (request, body, execution) -> {
            final var addedUri = fromUri(request.getURI())
                    .queryParam("language", "ko")
                    .build(true).toUri();
            return execution.execute(new HttpRequestWrapper(request) {
                @Override
                public URI getURI() {
                    return addedUri;
                }
            }, body);
        };
    }

    private Consumer<List<HttpMessageConverter<?>>> addSnakeCaseConverter(Jackson2ObjectMapperBuilder builder) {
        final var objectMapper = builder.propertyNamingStrategy(SNAKE_CASE).build();

        return converters -> converters.add(0, new MappingJackson2HttpMessageConverter(objectMapper));
    }
}
