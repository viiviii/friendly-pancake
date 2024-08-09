package com.pancake.api.bookmark.api;

import com.pancake.api.bookmark.application.BookmarkCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/bookmarks/customs")
@RequiredArgsConstructor
public class BookmarkCustomApiController {

    private final BookmarkCustom bookmarkCustom;

    @PostMapping
    public ResponseEntity<Void> post(@RequestBody BookmarkCustom.Command command) {
        bookmarkCustom.command(command);

        return status(CREATED).build();
    }
}
