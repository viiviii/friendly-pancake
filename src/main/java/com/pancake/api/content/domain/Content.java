package com.pancake.api.content.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;


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

    private boolean watched;

    public Content(String url, String title) {
        this(null, url, title, false);
    }

    public Long id() {
        return this.id;
    }

    public String url() {
        return this.url;
    }

    public String title() {
        return this.title;
    }

    public boolean isWatched() {
        return this.watched;
    }
}
