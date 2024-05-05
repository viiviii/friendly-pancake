package com.pancake.api.content.infra;

import com.pancake.api.content.application.ContentMetadata;
import com.pancake.api.search.FindContentMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
class ContentMetadataRepository implements FindContentMetadata {

    private final TmdbApiClient tmdbApiClient;

    @Override
    public Page<ContentMetadata> findAllByTitle(String title) {
        final var response = tmdbApiClient.searchMoviesBy(title);

        return response.toPage().map(SearchMovieResult::toMetadata);
    }
}
