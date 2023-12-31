package com.pancake.api.content.helper;

import com.pancake.api.content.application.dto.ContentRequest;
import lombok.Builder;

public class ContentRequestBuilders {

    @Builder
    private static ContentRequest createBuilder(String url, String title, String description, String imageUrl) {
        return new ContentRequest(url, title, description, imageUrl);
    }

    public static ContentRequestBuilder aRequest() {
        return ContentRequestBuilders.builder()
                .title("테스트용 제목")
                .description("테스트용 설명")
                .url("테스트용 주소")
                .imageUrl("테스트용 이미지 주소");
    }
}