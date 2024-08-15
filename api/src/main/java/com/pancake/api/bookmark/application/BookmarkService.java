package com.pancake.api.bookmark.application;

import com.pancake.api.bookmark.domain.Bookmark;
import com.pancake.api.bookmark.domain.BookmarkContent;
import com.pancake.api.bookmark.domain.BookmarkRepository;
import com.pancake.api.content.GetContent;
import com.pancake.api.content.application.ContentMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final GetContent getContent;

    private final BookmarkRepository bookmarkRepository;

    public Bookmark save(Bookmark bookmark) {
        validateInput(bookmark);

        return bookmarkRepository.save(bookmark);
    }

    public List<Bookmark> getList() {
        return bookmarkRepository.findAll();
    }

    private void validateInput(Bookmark input) {
        final var actual = getContentWith(input.getContent());

        if (actual == null || !actual.getTitle().equals(input.getRecordTitle())) {
            throw new IllegalArgumentException();
        }
    }

    private ContentMetadata getContentWith(BookmarkContent bookmarkContent) {
        final var query = new GetContent.Query(bookmarkContent.id(), bookmarkContent.type());

        return getContent.query(query);
    }
}
