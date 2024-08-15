package com.pancake.api.bookmark.application;

import com.pancake.api.bookmark.domain.Bookmark;
import com.pancake.api.bookmark.domain.BookmarkContent;
import com.pancake.api.content.domain.Content;
import com.pancake.api.content.domain.ContentRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.pancake.api.content.ContentType.custom;

@Service
@RequiredArgsConstructor
public class BookmarkCustom {
    private final ContentRepository contentRepository;
    private final BookmarkService bookmarkService;


    public Bookmark command(BookmarkCustom.Command command) {
        final var savedContent = saveContentWith(command);

        return bookmarkThe(savedContent);
    }

    private Content saveContentWith(BookmarkCustom.Command command) {
        final var content = new Content(command.title(), command.description(), command.imageUrl());

        return contentRepository.save(content);
    }

    private Bookmark bookmarkThe(Content content) {
        final var bookmarkContent = new BookmarkContent(content.getId().toString(), custom); // TODO: type이 왜 흩어져 계신지..
        final var bookmark = new Bookmark(bookmarkContent, content.getTitle());

        return bookmarkService.save(bookmark);
    }

    @Builder
    public record Command(String title, String imageUrl, String description) {
    }
}
