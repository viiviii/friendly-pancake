package com.pancake.api.content.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "playbacks")
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

    public Playback(String playbackUrl, Platform platform) {
        this(null, null, platform, new PlaybackUrl(playbackUrl));
    }

    private Playback(Long id, Long contentId, Platform platform, PlaybackUrl playbackUrl) {
        mustMatch(playbackUrl, platform);

        this.id = id;
        this.contentId = contentId;
        this.platform = platform;
        this.playbackUrl = playbackUrl;
    }

    private void mustMatch(PlaybackUrl playbackUrl, Platform platform) {
        if (!playbackUrl.toString().startsWith(platform.baseUrl())) {
            throw new IllegalArgumentException();
        }
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
