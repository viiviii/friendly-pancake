package com.pancake.api.bookmark;

import lombok.Builder;

public abstract class Builders {

    public static Builders.BookmarkSaveCommandBuilder aBookmarkSaveCommand() {
        return saveBookmarkCommand();
    }

    @Builder(builderMethodName = "saveBookmarkCommand")
    private static BookmarkSaveCommand create(String contentSource, String contentId, String contentType, String title) {
        return new BookmarkSaveCommand(contentSource, contentId, contentType, title);
    }
}