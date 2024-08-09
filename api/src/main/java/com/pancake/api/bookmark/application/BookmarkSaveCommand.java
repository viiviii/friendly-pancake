package com.pancake.api.bookmark.application;

import com.pancake.api.bookmark.domain.Bookmark;
import lombok.Builder;

@Builder
public record BookmarkSaveCommand(String contentId, String contentType, String title) {

    public Bookmark toBookmark() {
        return new Bookmark(null, contentId(), contentType(), title());
    }
}
