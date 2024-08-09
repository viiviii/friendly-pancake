package com.pancake.api.bookmark.application;

import com.pancake.api.bookmark.domain.Bookmark;
import com.pancake.api.bookmark.domain.BookmarkRepository;
import com.pancake.api.content.GetContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final GetContent getContent;

    private final BookmarkRepository bookmarkRepository;

    public Bookmark save(BookmarkSaveCommand command) {
        final var content = getContent.queryBy(command.contentId(), command.contentType());
        if (content == null || !content.getTitle().equals(command.title())) {
            throw new IllegalArgumentException();
        }
        final var bookmark = command.toBookmark();

        return bookmarkRepository.save(bookmark);
    }

    public List<Bookmark> getList() {
        return bookmarkRepository.findAll();
    }
}
