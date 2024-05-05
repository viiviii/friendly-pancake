package com.pancake.api.content.infra;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("https://api.themoviedb.org/3")
interface TmdbApiClient {

    @GetExchange("/search/movie")
    TmdbPage<SearchMovieResult> searchMoviesBy(@RequestParam String query);
}