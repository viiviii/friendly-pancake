package com.pancake.api.content.infra;

import com.pancake.api.content.domain.Content;
import com.pancake.api.helper.MemoryRepository;

import java.util.List;

import static java.util.function.Predicate.not;

public class MemoryContentRepository extends MemoryRepository<Content> implements ContentRepository {

    @Override
    public List<Content> findByWatchedFalse() {
        return this.findBy(not(Content::isWatched));
    }

    @Override
    public List<Content> findByWatchedTrue() {
        return this.findBy(Content::isWatched);
    }
}
