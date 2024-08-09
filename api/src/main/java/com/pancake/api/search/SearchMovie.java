package com.pancake.api.search;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public interface SearchMovie {

    Result query(String title);

    @Builder
    record Result(boolean hasNext, List<ResultItem> contents) {
    }

    @Builder
    record ResultItem(String id, String mediaType, String title, String originalTitle,
                      String description, String imageUrl, LocalDate releaseDate) {
    }
}