package com.pancake.api.watch;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.http.HttpStatus.SEE_OTHER;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequiredArgsConstructor
public class GetWatchApi {

    private final LoadPlayback loadPlayback;

    @GetMapping("/api/watch/{id}")
    public ResponseEntity<Void> redirectToPUrl(@PathVariable Long id) {
        final var playback = loadPlayback.query(id);

        return status(SEE_OTHER).location(URI.create(playback.getUrl())).build();
    }
}
