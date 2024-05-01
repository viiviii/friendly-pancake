package com.pancake.api.content.infra;

import com.pancake.api.content.application.ContentMetadata;
import com.pancake.api.search.FindContentMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component // TODO
@RequiredArgsConstructor
class TmdbApiClient implements FindContentMetadata {

    private final RestClient client;

    @Override
    public Page<ContentMetadata> findAllByTitle(String title) {
        final var response = client.get()
                .uri("/search/movie?query={title}&language=ko", title)
                .retrieve()
                .body(new ParameterizedTypeReference<TmdbPage<SearchMovieResult>>() {
                });

        // TODO: response null 체크, error status code 처리
        return response.toPage().map(SearchMovieResult::toMetadata);
    }
}