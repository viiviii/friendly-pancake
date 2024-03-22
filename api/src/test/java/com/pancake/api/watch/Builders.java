package com.pancake.api.watch;

import com.pancake.api.content.domain.Platform;
import com.pancake.api.watch.domain.WatchContent;
import com.pancake.api.watch.domain.WatchOption;
import lombok.Builder;
import lombok.Singular;

import java.util.List;

public abstract class Builders {

    public static Builders.WatchContentBuilder aWatchContent() {
        return watchContentBuilder()
                .id(999L)
                .title("테스트용 제목")
                .description("테스트용 설명")
                .imageUrl("테스트용 이미지 주소");
    }

    public static Builders.WatchOptionBuilder aWatchOption() {
        return watchOptionBuilder()
                .url("https://www.netflix.com/watch/100000001")
                .platform(Platform.NETFLIX);
    }

    @Builder(builderMethodName = "watchContentBuilder")
    private static WatchContent create(Long id, String title, String description, String imageUrl, boolean isWatched,
                                       @Singular List<WatchOption> options) {
        return new WatchContent(id, title, description, imageUrl, isWatched, options);
    }

    @Builder(builderMethodName = "watchOptionBuilder")
    private static WatchOption create(Platform platform, String url) {
        return new WatchOption(platform, url);
    }

}