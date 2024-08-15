package com.pancake.api.bookmark.api;

import com.pancake.api.bookmark.domain.Bookmark;
import com.pancake.api.bookmark.domain.BookmarkContent;
import com.pancake.api.content.ContentType;
import lombok.Builder;

@Builder
public record BookmarkRequest(String contentId, ContentType contentType, String title) {

    public Bookmark toBookmark() {
        return new Bookmark(null, content(), title());
    }

    private BookmarkContent content() {
        return new BookmarkContent(contentId(), contentType());
    }
}
