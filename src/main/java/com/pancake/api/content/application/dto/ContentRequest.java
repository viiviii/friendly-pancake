package com.pancake.api.content.application.dto;

import com.pancake.api.content.domain.Content;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentRequest {

    private String url;
    private String title;

    public Content toEntity() {
        return new Content(url, title);
    }
}
