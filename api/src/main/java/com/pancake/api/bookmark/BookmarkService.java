package com.pancake.api.bookmark;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public Bookmark save(BookmarkSaveCommand command) {
        final var bookmark = command.toBookmark();

        return bookmarkRepository.save(bookmark);
    }
}
