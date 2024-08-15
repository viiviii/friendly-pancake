package com.pancake.api.content.infra.api;


import com.pancake.api.content.application.ContentMetadata;
import com.pancake.api.content.movie.FindMovie;
import com.pancake.api.search.SearchMovie;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ContentMetadataAdapter implements SearchMovie, FindMovie {

    private final TmdbClient tmdbApiClient;

    @Override
    public Result query(String title) {
        final var response = tmdbApiClient.searchMoviesBy(title);

        return toSearchMovieResult(response.toPage());
    }

    @Override
    public ContentMetadata findById(String id) {
        final var response = tmdbApiClient.getMovieBy(id);

        return toMetadata(response);
    }

    private ContentMetadata toMetadata(TmdbMovie movie) {
        return new ContentMetadata(movie.id() + "", "movie", // TODO
                movie.title(), movie.originalTitle(), movie.overview(), movie.posterUrl(), movie.releaseDate());
    }

    private SearchMovie.Result toSearchMovieResult(Page<TmdbMovie> response) {
        return Result.builder()
                .hasNext(response.hasNext())
                .contents(response.getContent().stream()
                        .map(this::toSearchMovieContent)
                        .toList())
                .build();
    }

    private SearchMovie.ResultItem toSearchMovieContent(TmdbMovie movie) {
        return SearchMovie.ResultItem.builder()
                .id(movie.id().toString())
                .mediaType("movie") // TODO: multi로 바꾸면 tv, movie로 옴
                .title(movie.title())
                .originalTitle(movie.originalTitle())
                .description(movie.overview())
                .imageUrl(movie.posterUrl())
                .releaseDate(movie.releaseDate())
                .build();
    }
}
