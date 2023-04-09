package com.pancake.api.content.api;

import com.pancake.api.content.application.ContentService;
import com.pancake.api.content.application.dto.ContentRequest;
import com.pancake.api.content.application.dto.ContentResponse;
import com.pancake.api.content.domain.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentApiController {

    private final ContentService contentService;

    @PostMapping
    public ResponseEntity<ContentResponse> saveContent(@RequestBody ContentRequest request) {
        final Content content = contentService.save(request);

        return status(CREATED).body(ContentResponse.fromEntity(content));
    }

    @GetMapping
    public ResponseEntity<List<ContentResponse>> getAllContents() {
        final List<Content> contents = contentService.getAllContents();

        return status(OK).body(contents.stream()
                .map(ContentResponse::fromEntity)
                .toList());
    }

    @GetMapping("/unwatched")
    public ResponseEntity<List<ContentResponse>> getUnwatchedContents() {
        final List<Content> contents = contentService.getUnwatchedContents();

        return status(OK).body(contents.stream()
                .map(ContentResponse::fromEntity)
                .toList());
    }

    @GetMapping("/watched")
    public ResponseEntity<List<ContentResponse>> getWatchedContents() {
        final List<Content> contents = contentService.getWatchedContents();

        return status(OK).body(contents.stream()
                .map(ContentResponse::fromEntity)
                .toList());
    }

    @PatchMapping("{id}/watch")
    public ResponseEntity<Boolean> patchWatchContent(@PathVariable Long id) {
        final boolean watched = contentService.watch(id);

        return status(OK).body(watched);
    }
}
