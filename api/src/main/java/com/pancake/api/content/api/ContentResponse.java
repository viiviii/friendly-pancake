package com.pancake.api.content.api;

import com.pancake.api.content.domain.Content;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContentResponse {

    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private boolean watched;

    public ContentResponse(Content content) {
        this.id = content.getId();
        this.title = content.getTitle();
        this.description = content.getDescription();
        this.imageUrl = content.getImageUrl();
        this.watched = content.isWatched();
    }
}
