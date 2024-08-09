package com.pancake.api;

import com.pancake.api.bookmark.FindMovie;
import com.pancake.api.content.Builders;
import com.pancake.api.content.application.ContentMetadata;
import com.pancake.api.search.SearchMovie;
import org.springframework.boot.test.context.TestComponent;

import java.util.HashMap;
import java.util.Map;

@TestComponent
public class MemoryMetadataRepository implements SearchMovie, FindMovie {

    private final Map<String, ContentMetadata> map = new HashMap<>();

    public void 존재한다(Builders.ContentMetadataBuilder builder) {
        var metadata = builder.build();
        map.put(metadata.getId(), metadata);
    }

    @Override
    public ContentMetadata findById(String id) {
        return map.get(id);
    }

    @Override
    public Result query(String title) {
        var page = map.values().stream()
                .filter(e -> e.getTitle().equals(title))
                .toList();
        return null; // TODO
    }
}
