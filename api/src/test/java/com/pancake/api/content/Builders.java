package com.pancake.api.content;

import com.pancake.api.content.application.ContentMetadata;
import com.pancake.api.content.application.ContentSaveCommand;
import com.pancake.api.content.application.ContentStreaming;
import com.pancake.api.content.domain.Platform;
import com.pancake.api.content.domain.Playback;
import lombok.Builder;

import java.time.LocalDate;

import static com.pancake.api.content.domain.Platform.NETFLIX;

public abstract class Builders {

    public static ContentMetadataBuilder aMetadata() {
        return contentMetadata()
                .id("99999")
                .contentType("movie")
                .title("테스트용 제목")
                .originalTitle("테스트용 원제목")
                .description("테스트용 설명")
                .imageUrl("테스트용 이미지 주소")
                .releaseDate(LocalDate.of(2099, 12, 31));
    }

    public static ContentStreamingBuilder aStreaming() {
        return contentStreaming()
                .url("https://www.netflix.com/watch/100000001");
    }

    public static PlaybackBuilder aPlayback() {
        return playback()
                .url("https://www.netflix.com/watch/100000001")
                .platform(NETFLIX);
    }

    public static ContentSaveCommand.ContentSaveCommandBuilder aContentSaveCommand() {
        return ContentSaveCommand.builder()
                .title("테스트용 제목")
                .description("테스트용 설명")
                .imageUrl("테스트용 이미지 주소");
    }

    @Builder(builderMethodName = "contentMetadata")
    private static ContentMetadata create(String id, String contentType, String title, String originalTitle, String description, String imageUrl, LocalDate releaseDate) {
        return new ContentMetadata(id, contentType, title, originalTitle, description, imageUrl, releaseDate);
    }

    @Builder(builderMethodName = "contentStreaming")
    private static ContentStreaming create(String url) {
        return new ContentStreaming(url);
    }

    @Builder(builderMethodName = "playback")
    private static Playback create(String url, Platform platform) {
        return new Playback(url, platform);
    }
}