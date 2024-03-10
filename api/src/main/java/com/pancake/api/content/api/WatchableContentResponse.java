package com.pancake.api.content.api;

import com.pancake.api.content.domain.Content;
import com.pancake.api.content.domain.Playback;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WatchableContentResponse {

    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private boolean watched;

    private List<PlaybackResponse> playbacks;

    public WatchableContentResponse(Content content) {
        this.id = content.getId();
        this.title = content.getTitle();
        this.description = content.getDescription();
        this.imageUrl = content.getImageUrl();
        this.watched = content.isWatched();
        this.playbacks = toPlaybacks(content.getPlaybacks());
    }

    private static List<PlaybackResponse> toPlaybacks(List<Playback> watchingOptions) {
        return watchingOptions.stream().map(PlaybackResponse::new).toList();
    }

    @Data
    @NoArgsConstructor
    public static class PlaybackResponse {
        private Long id;
        private String platformLabel;

        public PlaybackResponse(Playback playback) {
            this.id = playback.getId();
            this.platformLabel = playback.getPlatform().label();
        }
    }
}
