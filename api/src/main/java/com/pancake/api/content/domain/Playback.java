package com.pancake.api.content.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "playbacks")
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class Playback {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "content_id")
    private Long contentId;

    private PlaybackUrl playbackUrl;

    public Playback(PlaybackUrl playbackUrl) {
        this(null, null, playbackUrl);
    }

    public Long getId() {
        return this.id;
    }

    public PlaybackUrl getUrl() {
        return this.playbackUrl;
    }

    void setContent(Long contentId) {
        this.contentId = contentId;
    }
}
