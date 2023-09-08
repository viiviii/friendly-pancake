package com.pancake.api.content.helper;

import com.pancake.api.content.application.dto.ContentRequest;
import lombok.Builder;

public class ContentRequests {

    @Builder
    private static ContentRequest createBuilder(String url, String title, String description, String imageUrl) {
        return new ContentRequest(url, title, description, imageUrl);
    }

    public static ContentRequest DUMMY = ContentRequests.builder()
            .title("테스트용 제목")
            .description("테스트용 설명")
            .url("https://www.netflix.com/watch/0")
            .imageUrl("https://occ.nflxso.net/api/0")
            .build();

    public static ContentRequest IRON_MAN = ContentRequests.builder()
            .title("아이언맨")
            .description("앤서니 에드워드 스타크는 대량살상무기를 실험하던 중")
            .url("https://www.netflix.com/watch/1")
            .imageUrl("https://occ.nflxso.net/api/1")
            .build();

    public static ContentRequest THOR = ContentRequests.builder()
            .title("토르: 천둥의 신")
            .description("신으로 태어나 슈퍼히어로가 되다")
            .url("https://www.netflix.com/watch/2")
            .imageUrl("https://occ.nflxso.net/api/2")
            .build();
}