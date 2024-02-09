package com.pancake.api.content.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    // TODO
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "content_id")
    private final List<Playback> playbacks = new ArrayList<>();

    public Content(String title, String description, String imageUrl) {
        this(null, title, description, imageUrl, false);
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

    public List<Playback> getPlaybacks() {
        return this.playbacks;
    }

    public boolean isWatched() {
        return this.watched;
    }

    public void watch() {
        watched = true;
    }

    // TODO
    public void add(Playback playback) {
        this.playbacks.add(playback);
        playback.setContent(this.id);
    }

    public boolean canWatch() {
        return !getPlaybacks().isEmpty();
    }
}
