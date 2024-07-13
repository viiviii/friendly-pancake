package com.pancake.api.bookmark;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkApiController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<BookmarkResponse> save(@RequestBody BookmarkSaveCommand command) {
        final var bookmark = bookmarkService.save(command);

        return status(CREATED).body(new BookmarkResponse(bookmark));
    }

    public record BookmarkResponse(Long id, String contentId) {
        public BookmarkResponse(Bookmark bookmark) {
            this(bookmark.getId(), bookmark.getContentId());
        }
    }
}
