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

    private PlaybackUrl url;

    public Playback(String url, Platform platform) {
        this(null, null, platform, new PlaybackUrl(url));
    }

    private Playback(Long id, Long contentId, Platform platform, PlaybackUrl url) {
        mustMatch(url, platform);

        this.id = id;
        this.contentId = contentId;
        this.platform = platform;
        this.url = url;
    }

    private void mustMatch(PlaybackUrl url, Platform platform) {
        if (!url.isSatisfiedBy(platform)) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url.toString();
    }

    public Platform getPlatform() {
        return this.platform;
    }

    void setContent(Long contentId) {
        this.contentId = contentId;
    }
}
