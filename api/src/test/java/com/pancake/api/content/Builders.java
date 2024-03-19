package com.pancake.api.content;

import com.pancake.api.content.application.ContentMetadata;
import com.pancake.api.content.application.ContentStreaming;
import com.pancake.api.content.domain.Platform;
import com.pancake.api.content.domain.Playback;
import lombok.Builder;

import static com.pancake.api.content.domain.Platform.NETFLIX;

public abstract class Builders {

    public static ContentMetadataBuilder aMetadata() {
        return contentMetadataBuilder()
                .title("테스트용 제목")
                .description("테스트용 설명")
                .imageUrl("테스트용 이미지 주소");
    }

    public static ContentStreamingBuilder aStreaming() {
        return contentStreamingBuilder()
                .url("https://www.netflix.com/watch/100000001");
    }

    public static PlaybackBuilder aPlayback() {
        return playbackBuilder()
                .url("https://www.netflix.com/watch/100000001")
                .platform(NETFLIX);
    }

    @Builder(builderMethodName = "contentMetadataBuilder")
    private static ContentMetadata create(String title, String description, String imageUrl) {
        return new ContentMetadata(title, description, imageUrl);
    }

    @Builder(builderMethodName = "contentStreamingBuilder")
    private static ContentStreaming create(String url) {
        return new ContentStreaming(url);
    }

    @Builder(builderMethodName = "playbackBuilder")
    private static Playback create(String url, Platform platform) {
        return new Playback(url, platform);
    }
}