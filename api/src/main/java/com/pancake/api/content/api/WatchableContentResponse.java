package com.pancake.api.content.api;

import com.pancake.api.content.domain.Content;
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
    private String url;
    private String imageUrl;
    private boolean watched;

    private List<WatchResponse> watches;

    public static WatchableContentResponse fromEntity(Content content) {
        final var watchlist = List.of(new WatchResponse(999L, "넷플릭스"));
        return new WatchableContentResponse(content.getId(), content.getTitle(), content.getDescription(),
                content.getPlaybackUrl().asString(), content.getImageUrl(), content.isWatched(),
                watchlist);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WatchResponse {
        private Long id;
        private String platformName;
    }
}
