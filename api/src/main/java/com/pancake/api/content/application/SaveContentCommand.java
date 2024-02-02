package com.pancake.api.content.application;

import com.pancake.api.content.domain.Content;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveContentCommand {

    private String title;
    private String description;
    private String imageUrl;

    public Content toEntity() {
        return new Content(title, description, imageUrl);
    }
}
