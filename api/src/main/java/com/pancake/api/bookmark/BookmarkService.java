package com.pancake.api.bookmark;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final GetContentMetadata getContentMetadata;

    private final BookmarkRepository bookmarkRepository;

    public Bookmark save(BookmarkSaveCommand command) {
        final var metadata = getContentMetadata.queryBy(command.contentId());
        if (metadata == null || !metadata.getTitle().equals(command.title())) {
            throw new IllegalArgumentException();
        }
        final var bookmark = command.toBookmark();

        return bookmarkRepository.save(bookmark);
    }

    public List<Bookmark> getList() {
        return bookmarkRepository.findAll();
    }
}
