package com.pancake.api.watch;

import com.pancake.api.content.domain.Playback;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class LoadPlayback {

    @PersistenceContext
    private final EntityManager em;

    public Playback query(Long id) {
        final var playback = em.find(Playback.class, id);

        if (playback == null) {
            throw new IllegalArgumentException();
        }

        return playback;
    }
}