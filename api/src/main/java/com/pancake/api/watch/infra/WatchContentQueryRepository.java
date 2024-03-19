package com.pancake.api.watch.infra;

import com.pancake.api.content.domain.Content;
import com.pancake.api.content.domain.ContentRepository;
import com.pancake.api.content.domain.Playback;
import com.pancake.api.watch.domain.WatchContent;
import com.pancake.api.watch.domain.WatchContentRepository;
import com.pancake.api.watch.domain.WatchOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class WatchContentQueryRepository implements WatchContentRepository {

    private final ContentRepository contentRepository; // TODO

    @Override
    public List<WatchContent> findAll() {
        return contentRepository.findAll().stream().map(this::mapToContent).toList();
    }

    @Override
    public Optional<WatchContent> findById(long id) {
        return contentRepository.findById(id).map(this::mapToContent);
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
