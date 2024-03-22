package com.pancake.api.watch.infra;

import com.pancake.api.content.domain.Platform;
import com.pancake.api.content.domain.Playback;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import java.util.Optional;

interface JpaPlaybackQueryRepository extends Repository<Playback, Long> {

    @EntityGraph(attributePaths = {"content"})
    Optional<Playback> findByContentIdAndPlatform(Long contentId, Platform platform);
}
