package com.pancake.api.bookmark.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "bookmarks")
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Getter
@EqualsAndHashCode
public class Bookmark {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Embedded
    private BookmarkContent content;

    private String recordTitle;

    public Bookmark(BookmarkContent content, String recordTitle) {
        this(null, content, recordTitle);
    }
}
