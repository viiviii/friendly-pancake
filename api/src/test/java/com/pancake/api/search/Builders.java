package com.pancake.api.search;

import java.util.Collections;

public abstract class Builders {

    public static SearchMovie.Result.ResultBuilder aSearchMovieResult() {
        return SearchMovie.Result.builder()
                .hasNext(false)
                .contents(Collections.emptyList());
    }

    public static SearchMovie.ResultItem.ResultItemBuilder aSearchMovieContent() {
        return SearchMovie.ResultItem.builder()
                .id("9999")
                .mediaType("movie")
                .title("테스트용 제목")
                .originalTitle("테스트용 원제")
                .description("테스트용 오버뷰")
                .imageUrl("테스트용 이미지 주소")
                .releaseDate(null);
    }
}