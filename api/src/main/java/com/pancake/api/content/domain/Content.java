package com.pancake.api.content.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;


@Entity
@Table(name = "contents")
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Content {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String imageUrl;

    private boolean watched;

    @OneToMany(mappedBy = "content", cascade = PERSIST, fetch = LAZY)
    private final List<Playback> playbacks = new ArrayList<>();

    public Content(String title, String description, String imageUrl) {
        this(null, title, description, imageUrl, false);
    }

    public boolean isWatched() {
        return this.watched;
    }


    // TODO: 얘도 제거해야돼
    public void watch() {
        watched = true;
    }

    public void add(Playback playback) {
        this.playbacks.add(playback);
        playback.setContent(this);
    }

    public void change(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
