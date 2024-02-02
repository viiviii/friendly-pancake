package com.pancake.api.content.application;

import lombok.Builder;

public class SaveContentCommandBuilders {

    @Builder
    private static SaveContentCommand createBuilder(String title, String description, String imageUrl) {
        return new SaveContentCommand(title, description, imageUrl);
    }

    public static SaveContentCommandBuilder aSaveContentCommand() {
        return SaveContentCommandBuilders.builder()
                .title("테스트용 제목")
                .description("테스트용 설명")
                .imageUrl("테스트용 이미지 주소");
    }
}