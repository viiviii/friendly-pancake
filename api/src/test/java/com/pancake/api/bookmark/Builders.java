package com.pancake.api.bookmark;

public abstract class Builders {

    public static BookmarkSaveCommand.BookmarkSaveCommandBuilder aBookmarkSaveCommand() {
        return BookmarkSaveCommand.builder()
                .contentId("9999")
                .contentType("movie")
                .title("기록용 타이틀");
    }
}