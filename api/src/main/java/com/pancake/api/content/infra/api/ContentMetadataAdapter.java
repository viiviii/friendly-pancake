package com.pancake.api.content.infra.api;

import com.pancake.api.content.application.ContentMetadata;
import com.pancake.api.search.FindContentMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ContentMetadataAdapter implements FindContentMetadata {

    private final TmdbClient tmdbApiClient;

    @Override
    public Page<ContentMetadata> findAllByTitle(String title) {
        final var response = tmdbApiClient.searchMoviesBy(title);

        return response.toPage().map(this::toMetadata);
    }

    private ContentMetadata toMetadata(TmdbMovie movie) {
        return new ContentMetadata(movie.title(), movie.originalTitle(), movie.overview(), movie.posterUrl(), movie.releaseDate());
    }
}
