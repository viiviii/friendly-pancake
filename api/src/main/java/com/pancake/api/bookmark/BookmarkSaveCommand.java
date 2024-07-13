package com.pancake.api.bookmark;

public record BookmarkSaveCommand(String contentSource, String contentId, String contentType, String title) {

    public Bookmark toBookmark() {
        return new Bookmark(null, contentSource(), contentId(), contentType(), title());
    }
}
