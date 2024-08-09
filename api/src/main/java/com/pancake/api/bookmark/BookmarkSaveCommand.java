package com.pancake.api.bookmark;

import lombok.Builder;

@Builder
public record BookmarkSaveCommand(String contentId, String contentType, String title) {

    public Bookmark toBookmark() {
        return new Bookmark(null, contentId(), contentType(), title());
    }
}
