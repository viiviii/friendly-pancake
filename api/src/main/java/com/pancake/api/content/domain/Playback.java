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

    @Enumerated(EnumType.STRING)
    private Platform platform;

    private PlaybackUrl playbackUrl;

    public Playback(PlaybackUrl playbackUrl, Platform platform) {
        this(null, null, platform, playbackUrl);
    }

    public Long getId() {
        return this.id;
    }

    public String getUrl() {
        return this.playbackUrl.toString();
    }

    public Platform getPlatform() {
        return this.platform;
    }

    void setContent(Long contentId) {
        this.contentId = contentId;
    }
}
