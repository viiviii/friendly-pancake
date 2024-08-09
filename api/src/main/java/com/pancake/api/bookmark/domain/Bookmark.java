package com.pancake.api.bookmark.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "bookmarks")
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Bookmark {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String contentId;

    private String contentType;

    private String recordTitle;
}
