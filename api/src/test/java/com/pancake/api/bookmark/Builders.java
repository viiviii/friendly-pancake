package com.pancake.api.bookmark;

import lombok.Builder;

public abstract class Builders {

    public static Builders.BookmarkSaveCommandBuilder aBookmarkSaveCommand() {
        return saveBookmarkCommand()
                .contentId("9999")
                .contentSource("TMDB")
                .contentType("movie")
                .title("테스트용 타이틀");
    }

    @Builder(builderMethodName = "saveBookmarkCommand")
    private static BookmarkSaveCommand create(String contentSource, String contentId, String contentType, String title) {
        return new BookmarkSaveCommand(contentSource, contentId, contentType, title);
    }
}