package com.pancake.api.bookmark.api;

import com.pancake.api.bookmark.domain.Bookmark;

public record BookmarkResponse(Long id, String contentId, String contentType, String recordTitle) {
    public BookmarkResponse(Bookmark bookmark) {
        this(bookmark.getId(), bookmark.getContent().id(), bookmark.getContent().type().name(), bookmark.getRecordTitle());
    }
}
