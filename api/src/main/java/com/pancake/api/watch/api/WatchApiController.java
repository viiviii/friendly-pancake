package com.pancake.api.watch.api;

import com.pancake.api.watch.application.Catalog;
import com.pancake.api.watch.application.GetContentsToWatch;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/watches")
@RequiredArgsConstructor
public class WatchApiController {

    private final GetContentsToWatch getContentsToWatch;

    @GetMapping
    public ResponseEntity<Catalog> get() {
        final var catalog = getContentsToWatch.query();
        
        return status(OK).body(catalog);
    }
}
