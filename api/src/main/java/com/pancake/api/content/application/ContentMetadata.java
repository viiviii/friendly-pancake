package com.pancake.api.content.application;

import com.pancake.api.content.domain.Content;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentMetadata {

    private String title;
    private String originalTitle;
    private String description;
    private String imageUrl;
    private LocalDate releaseDate;

    public Content toContent() {
        return new Content(title, description, imageUrl);
    }
}
