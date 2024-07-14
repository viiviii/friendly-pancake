package com.pancake.api.bookmark;

public record BookmarkResponse(Long id, String contentId, String contentType, String recordTitle) {
    public BookmarkResponse(Bookmark bookmark) {
        this(bookmark.getId(), bookmark.getContentId(), bookmark.getContentType(), bookmark.getRecordTitle());
    }
}
