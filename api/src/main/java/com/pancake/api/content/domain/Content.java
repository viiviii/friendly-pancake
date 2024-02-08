package com.pancake.api.content.domain;

import jakarta.persistence.*;
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

    private String title;

    private String description;

    private String imageUrl;

    private boolean watched;

    @Column(name = "url")
    private Watch watch;

    public Content(String title, String description, String imageUrl) {
        this(null, title, description, imageUrl, false, new Watch(new PlaybackUrl("https://www.netflix.com/"))); // TODO: watch=null
    }

    public Long getId() {
        return this.id;
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

    public PlaybackUrl getPlaybackUrl() {
        return this.watch.getPlaybackUrl();
    }

    public boolean isWatched() {
        return this.watched;
    }

    public void watch() {
        watched = true;
    }

    public void addWatch(Watch watch) {
        this.watch = watch;
    }

    public boolean canWatch() {
        return !getPlaybackUrl().asString().equals("https://www.netflix.com/"); // TODO
    }
}
