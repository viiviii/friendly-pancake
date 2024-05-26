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

    private final SearchContentMetadata searchContentMetadata;

    @GetMapping("contents")
    public ResponseEntity<SearchContentMetadata.Result> searchContents(@RequestParam String query) {
        final var result = searchContentMetadata.queryBy(query);

        return status(OK).body(result);
    }
}
