package com.pancake.api.bookmark;

import com.pancake.api.bookmark.api.BookmarkRequest;
import com.pancake.api.bookmark.application.BookmarkCustom;
import com.pancake.api.bookmark.domain.Bookmark;
import com.pancake.api.bookmark.domain.BookmarkContent;
import com.pancake.api.content.ContentType;
import lombok.Builder;

import static com.pancake.api.content.ContentType.movie;

public abstract class Builders {

    public static BookmarkRequest.BookmarkRequestBuilder aBookmarkCommand() {
        return BookmarkRequest.builder()
                .contentId("9999")
                .contentType(movie)
                .title("기록용 타이틀");
    }

    public static BookmarkCustom.Command.CommandBuilder aBookmarkCustom() {
        return BookmarkCustom.Command.builder()
                .title("북마크할 컨텐츠 제목")
                .imageUrl("북마크할 컨텐츠의 이미지 주소")
                .description("북마크할 컨텐츠의 설명");
    }

    public static BookmarkContent.BookmarkContentBuilder aBookmarkContent() {
        return BookmarkContent.builder().id("9999").type(movie);
    }

    public static BookmarkBuilder aBookmark() {
        return bookmarkBuilder().contentId("9999").contentType(movie);
    }

    @Builder(builderMethodName = "bookmarkBuilder")
    private static Bookmark create(String contentId, ContentType contentType, String title) {
        return new Bookmark(new BookmarkContent(contentId, contentType), title);
    }
}