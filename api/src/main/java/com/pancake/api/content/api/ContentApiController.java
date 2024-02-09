package com.pancake.api.content.api;

import com.pancake.api.content.application.AddPlaybackCommand;
import com.pancake.api.content.application.ContentService;
import com.pancake.api.content.application.SaveContentCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentApiController {

    private final ContentService contentService;

    @PostMapping
    public ResponseEntity<ContentResponse> save(@RequestBody SaveContentCommand command) {
        final var content = contentService.save(command);
        final var response = ContentResponse.fromEntity(content);

        return status(CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<WatchableContentResponse>> getAll() {
        final var contents = contentService.getAllContents();
        final var response = contents.stream().map(WatchableContentResponse::fromEntity).toList();

        return status(OK).body(response);
    }

    // TODO
    @GetMapping("{id}")
    public ResponseEntity<Void> getById(@PathVariable Long id) {
        final var content = contentService.getContent(id);

        return status(SEE_OTHER).location(URI.create("")).build();
    }

    @PostMapping("{id}/playbacks")
    public ResponseEntity<Void> addPlayback(@PathVariable Long id, @RequestBody AddPlaybackCommand command) {
        contentService.addPlayback(id, command);

        return status(NO_CONTENT).build();
    }

    @PatchMapping("{id}/watched")
    public ResponseEntity<Void> changeContentToWatch(@PathVariable Long id) {
        contentService.watch(id);

        return status(NO_CONTENT).build();
    }
}
