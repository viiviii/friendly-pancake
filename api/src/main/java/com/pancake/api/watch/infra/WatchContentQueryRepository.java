package com.pancake.api.watch.infra;

import com.pancake.api.content.domain.Content;
import com.pancake.api.content.domain.Platform;
import com.pancake.api.content.domain.Playback;
import com.pancake.api.watch.domain.FindWatchOption;
import com.pancake.api.watch.domain.WatchContent;
import com.pancake.api.watch.domain.WatchContentRepository;
import com.pancake.api.watch.domain.WatchOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
class WatchContentQueryRepository implements WatchContentRepository, FindWatchOption {

    private final JpaContentQueryRepository contents;
    private final JpaPlaybackQueryRepository playbacks;

    @Override
    public List<WatchContent> findAll() {
        return contents.findAll().stream()
                .map(this::mapToContent)
                .toList();
    }

    @Override
    public Optional<WatchOption> findByContentIdAndPlatform(Long contentId, Platform platform) {
        return playbacks.findByContentIdAndPlatform(contentId, platform)
                .map(this::mapToOption);
    }

    private WatchContent mapToContent(Content content) {
        return new WatchContent(
                content.getId(),
                content.getTitle(),
                content.getDescription(),
                content.getImageUrl(),
                content.isWatched(),
                mapToOptions(content)
        );
    }

    private List<WatchOption> mapToOptions(Content content) {
        return content.getPlaybacks().stream().map(this::mapToOption).toList();
    }

    private WatchOption mapToOption(Playback entity) {
        return new WatchOption(
                entity.getPlatform(),
                entity.getUrl()
        );
    }
}
