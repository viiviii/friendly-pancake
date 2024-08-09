package com.pancake.api.search;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchApiController {

    private final SearchMovie searchMovie;

    @GetMapping("contents")
    public ResponseEntity<SearchMovie.Result> get(@RequestParam String query) {
        final var result = searchMovie.query(query);

        return status(OK).body(result);
    }
}
