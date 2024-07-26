package com.pancake.api.content.application;

import com.pancake.api.content.domain.Content;
import lombok.Builder;

@Builder
public record ContentSaveCommand(String title, String imageUrl, String description) {

    public Content toContent() {
        return new Content(title, description, imageUrl);
    }
}
