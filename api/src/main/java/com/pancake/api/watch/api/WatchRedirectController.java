package com.pancake.api.watch.api;

import com.pancake.api.content.domain.Platform;
import com.pancake.api.watch.application.GetWatchUrl;
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
public class WatchRedirectController {

    private final GetWatchUrl getWatchUrl;

    @GetMapping("/api/watch/{id}/{platform}")
    public ResponseEntity<Void> redirect(@PathVariable Long id, @PathVariable Platform platform) {
        final var url = getWatchUrl.query(id, platform);

        return status(SEE_OTHER).location(URI.create(url)).build();
    }
}
