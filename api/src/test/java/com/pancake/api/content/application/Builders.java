package com.pancake.api.content.application;

import com.pancake.api.content.domain.Platform;
import com.pancake.api.content.domain.Playback;
import lombok.Builder;

import static com.pancake.api.content.domain.Platform.NETFLIX;

public abstract class Builders {

    public static SaveContentCommandBuilder aContentToSave() {
        return saveContentCommandBuilder()
                .title("테스트용 제목")
                .description("테스트용 설명")
                .imageUrl("테스트용 이미지 주소");
    }

    public static AddPlaybackCommandBuilder aPlaybackToAdd() {
        return addPlaybackCommandBuilder()
                .url("https://www.netflix.com/watch/100000001");
    }

    public static PlaybackBuilder aPlayback() {
        return playbackBuilder()
                .url("https://www.netflix.com/watch/100000001")
                .platform(NETFLIX);
    }

    @Builder(builderMethodName = "saveContentCommandBuilder")
    private static SaveContentCommand create(String title, String description, String imageUrl) {
        return new SaveContentCommand(title, description, imageUrl);
    }

    @Builder(builderMethodName = "addPlaybackCommandBuilder")
    private static AddPlaybackCommand create(String url) {
        return new AddPlaybackCommand(url);
    }

    @Builder(builderMethodName = "playbackBuilder")
    private static Playback create(String url, Platform platform) {
        return new Playback(url, platform);
    }
}