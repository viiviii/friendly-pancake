package com.pancake.api.content.infra.api;

import org.junit.jupiter.api.Test;

import static com.pancake.api.content.Builders.aTmdbMovie;
import static org.assertj.core.api.Assertions.assertThat;

class TmdbMovieTest {

    @Test
    void posterUrl() {
        //given
        var movie = aTmdbMovie().posterPath("/some").build();

        //when
        var actual = movie.posterUrl();

        //then
        assertThat(actual).isEqualTo("https://image.tmdb.org/t/p/w500/some");
    }
}