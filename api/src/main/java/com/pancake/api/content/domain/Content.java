package com.pancake.api.content.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.StringUtils.hasText;


@Entity
@Table(name = "contents")
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Content {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String url;

    private String title;

    private String description;

    private String imageUrl;

    private boolean watched;

    public Content(String title, String description, String imageUrl) {
        this(null, "", title, description, imageUrl, false); // TODO: url=""
    }

    public Long getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public boolean isWatched() {
        return this.watched;
    }

    public void watch() {
        watched = true;
    }

    public void addUrl(String url) {
        this.url = url;
    }

    public boolean canWatch() {
        return hasText(getUrl());
    }
}
