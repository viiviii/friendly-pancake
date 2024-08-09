package com.pancake.api.bookmark;

public abstract class Builders {

    public static BookmarkSaveCommand.BookmarkSaveCommandBuilder aBookmarkSaveCommand() {
        return BookmarkSaveCommand.builder()
                .contentId("9999")
                .contentType("movie")
                .title("기록용 타이틀");
    }

    public static BookmarkCustomContent.Command.CommandBuilder aBookmarkCustom() {
        return BookmarkCustomContent.Command.builder()
                .title("북마크할 컨텐츠 제목")
                .imageUrl("북마크할 컨텐츠의 이미지 주소")
                .description("북마크할 컨텐츠의 설명");
    }
}