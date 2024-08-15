package com.pancake.api.content.infra.api;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record TmdbMovie(
        Boolean adult, String backdropPath, List<Integer> genreIds, Integer id,
        String originalLanguage, String originalTitle, String overview, Float popularity,
        String posterPath, LocalDate releaseDate, String title, Boolean video, Float voteAverage, Integer voteCount) {

    public String posterUrl() {
        return String.format("https://image.tmdb.org/t/p/w500%s", posterPath());
    }
}
