package com.pancake.api.content.application;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentMetadata {

    private String id;
    private String contentType; // TODO: 이거 여기 맞냐
    private String title;
    private String originalTitle; // TODO: 필요없음
    private String description;
    private String imageUrl;
    private LocalDate releaseDate;
}
