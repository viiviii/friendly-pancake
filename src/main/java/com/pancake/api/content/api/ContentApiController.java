package com.pancake.api.content.api;

import com.pancake.api.content.application.ContentService;
import com.pancake.api.content.application.dto.ContentRequest;
import com.pancake.api.content.application.dto.ContentResponse;
import com.pancake.api.content.domain.Content;
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

    @GetMapping("{id}")
    public ResponseEntity<Void> getContentById(@PathVariable Long id) {
        final var content = contentService.getContent(id);

        return status(SEE_OTHER).location(URI.create(content.url())).build();
    }

    @PatchMapping("{id}/watch")
    public ResponseEntity<Boolean> patchWatchContent(@PathVariable Long id) {
        final boolean watched = contentService.watch(id);

        return status(OK).body(watched);
    }
}
