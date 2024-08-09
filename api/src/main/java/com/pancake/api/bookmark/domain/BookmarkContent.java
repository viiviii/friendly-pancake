package com.pancake.api.bookmark.domain;

import com.pancake.api.content.ContentType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.Builder;

import static jakarta.persistence.EnumType.STRING;

@Embeddable
@Builder
public record BookmarkContent(
        @Column(name = "content_id")
        String id,

        @Column(name = "content_type")
        @Enumerated(STRING)
        ContentType type) {
}

