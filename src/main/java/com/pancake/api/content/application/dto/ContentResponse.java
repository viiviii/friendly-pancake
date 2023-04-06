package com.pancake.api.content.application.dto;

import com.pancake.api.content.domain.Content;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentResponse {

    private Long id;
    private String title;
    private String url;

    public static ContentResponse fromEntity(Content content) {
        return new ContentResponse(content.id(), content.title(), content.url());
    }
}
