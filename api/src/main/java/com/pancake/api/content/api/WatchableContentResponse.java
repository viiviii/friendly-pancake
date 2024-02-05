package com.pancake.api.content.api;

import com.pancake.api.content.domain.Content;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public static WatchableContentResponse fromEntity(Content content) {
        return new WatchableContentResponse(content.getId(), content.getTitle(), content.getDescription(),
                content.getUrl(), content.getImageUrl(), content.isWatched());
    }
}
