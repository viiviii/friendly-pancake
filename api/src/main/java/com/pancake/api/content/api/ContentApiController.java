package com.pancake.api.content.api;

import com.pancake.api.content.application.AddPlayback;
import com.pancake.api.content.application.ContentMetadata;
import com.pancake.api.content.application.ContentService;
import com.pancake.api.content.application.ContentStreaming;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentApiController {

    private final ContentService contentService;

    private final AddPlayback addPlayback;

    @PostMapping
    public ResponseEntity<ContentResponse> save(@RequestBody ContentMetadata metadata) {
        final var content = contentService.save(metadata);
        final var response = new ContentResponse(content);

        return status(CREATED).body(response);
    }

    @PostMapping("{id}/playbacks")
    public ResponseEntity<Void> addPlayback(@PathVariable Long id, @RequestBody ContentStreaming streaming) {
        addPlayback.command(id, streaming);

        return status(NO_CONTENT).build();
    }

    @PatchMapping("{id}/watched")
    public ResponseEntity<Void> changeContentToWatch(@PathVariable Long id) {
        contentService.watch(id);

        return status(NO_CONTENT).build();
    }

    @PatchMapping("{id}/image")
    public ResponseEntity<Void> changeContentImage(@PathVariable Long id, @RequestBody String imageUrl) {
        contentService.changeImage(id, imageUrl);

        return status(NO_CONTENT).build();
    }
}
