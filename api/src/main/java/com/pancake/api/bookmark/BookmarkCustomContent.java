package com.pancake.api.bookmark;

import com.pancake.api.content.domain.Content;
import com.pancake.api.content.domain.ContentRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkCustomContent {
    private final ContentRepository contentRepository;
    private final BookmarkService bookmarkService;


    public Bookmark command(Command command) {
        final var content = contentRepository.save(toContent(command));

        return bookmarkService.save(toBookmark(content));
    }

    private Content toContent(Command command) {
        return new Content(command.title(), command.description(), command.imageUrl());
    }

    private BookmarkSaveCommand toBookmark(Content content) {
        return BookmarkSaveCommand.builder()
                .contentId(content.getId().toString())
                .contentType("custom")
                .title(content.getTitle())
                .build();
    }

    @Builder
    public record Command(String title, String imageUrl, String description) {
    }
}
