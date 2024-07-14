package com.pancake.api.bookmark;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
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

    @GetMapping
    public ResponseEntity<List<BookmarkResponse>> getList() {
        final var response = bookmarkService.getList().stream()
                .map(BookmarkResponse::new)
                .toList();

        return status(OK).body(response);
    }

    public record BookmarkResponse(Long id, String contentId, String contentType, String recordTitle) {
        public BookmarkResponse(Bookmark bookmark) {
            this(bookmark.getId(), bookmark.getContentId(), bookmark.getContentType(), bookmark.getRecordTitle());
        }
    }
}
