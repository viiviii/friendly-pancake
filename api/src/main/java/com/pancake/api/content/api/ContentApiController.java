package com.pancake.api.content.api;

import com.pancake.api.content.application.AddPlayback;
import com.pancake.api.content.application.ContentSaveCommand;
import com.pancake.api.content.application.ContentService;
import com.pancake.api.content.application.ContentStreaming;
import com.pancake.api.content.domain.ContentRepository;
import com.pancake.api.content.domain.Playback;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentApiController {

    private final ContentService contentService;
    private final ContentRepository contentRepository;
    private final AddPlayback addPlayback;

    @GetMapping
    public ResponseEntity<List<ContentResponse>> getAll() {
        final var contents = contentRepository.findAll();
        final var response = contents.stream().map(ContentResponse::new).toList();

        return status(OK).body(response);
    }

    @PostMapping
    public ResponseEntity<ContentResponse> save(@RequestBody ContentSaveCommand command) {
        final var content = contentService.save(command);
        final var response = new ContentResponse(content);

        return status(CREATED).body(response);
    }

    @GetMapping("{id}/playbacks")
    public ResponseEntity<List<Playback>> getAllPlaybacksIn(@PathVariable Long id) {
        final var content = contentService.get(id);
        final var response = content.getPlaybacks();

        return status(OK).body(response);
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
