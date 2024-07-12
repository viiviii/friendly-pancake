package com.pancake.api.bookmark;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final GetContentMetadata getContentMetadata;

    private final BookmarkRepository bookmarkRepository;

    public Bookmark save(BookmarkSaveCommand command) {
        final var metadata = getContentMetadata.queryBy(command.contentId());
        if (!metadata.getTitle().equals(command.title())) {
            throw new IllegalArgumentException();
        }
        final var bookmark = command.toBookmark();

        return bookmarkRepository.save(bookmark);
    }
}
