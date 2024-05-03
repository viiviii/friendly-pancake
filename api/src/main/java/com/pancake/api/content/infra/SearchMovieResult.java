package com.pancake.api.content.infra;

import com.pancake.api.content.application.ContentMetadata;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder(builderClassName = "Builder")
public record SearchMovieResult(boolean adult, String backdropPath, List<Integer> genreIds, int id,
                                String originalLanguage, String originalTitle, String overview, float popularity,
                                String posterPath, LocalDate releaseDate, String title, boolean video,
                                float voteAverage, int voteCount) {

    ContentMetadata toMetadata() {
        return new ContentMetadata(title(), overview(), posterPath());
    }
}
