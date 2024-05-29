package com.pancake.api.content.infra.api;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder(builderClassName = "Builder")
public record TmdbMovie(
        boolean adult, String backdropPath, List<Integer> genreIds, int id,
        String originalLanguage, String originalTitle, String overview, float popularity,
        String posterPath, LocalDate releaseDate, String title, boolean video, float voteAverage, int voteCount) {

    public String posterUrl() {
        return String.format("https://image.tmdb.org/t/p/w500%s", posterPath());
    }
}
