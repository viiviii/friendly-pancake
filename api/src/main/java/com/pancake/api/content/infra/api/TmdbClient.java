package com.pancake.api.content.infra.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("https://api.themoviedb.org/3")
public interface TmdbClient {

    @GetExchange("/search/movie")
    TmdbPage<TmdbMovie> searchMoviesBy(@RequestParam String query);

    @GetExchange("/movie/{movieId}")
    TmdbMovie getMovieBy(@PathVariable String movieId);
}