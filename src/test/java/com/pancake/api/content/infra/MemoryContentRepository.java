package com.pancake.api.content.infra;

import com.pancake.api.content.domain.Content;
import com.pancake.api.helper.MemoryRepository;

import java.util.List;

public class MemoryContentRepository extends MemoryRepository<Content> implements ContentRepository {
    @Override
    public List<Content> findByWatchedFalse() {
        return this.findAll().stream()
                .filter(content -> !content.isWatched())
                .toList();
    }
}
