package com.pancake.api.content.api;

import com.pancake.api.content.domain.Content;
import com.pancake.api.content.domain.Playback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchableContentResponse {

    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private boolean watched;

    private List<PlaybackResponse> playbacks;

    public static WatchableContentResponse fromEntity(Content content) {
        final var playbackList = content.getPlaybacks().stream().map(PlaybackResponse::fromEntity).toList();
        return new WatchableContentResponse(content.getId(), content.getTitle(),
                content.getDescription(), content.getImageUrl(), content.isWatched(),
                playbackList);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlaybackResponse {
        private Long id;
        private String platformName; // TODO: label로 바꿔라

        public static PlaybackResponse fromEntity(Playback playback) {
            return new PlaybackResponse(playback.getId(), playback.getPlatform().label());
        }
    }
}
