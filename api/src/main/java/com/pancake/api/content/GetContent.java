package com.pancake.api.content;

import com.pancake.api.content.application.ContentMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class GetContent {

    private final List<ContentProvider> providers;

    public ContentMetadata query(Query query) {
        final var provider = providers.stream()
                .filter(isProviderFor(query.contentType()))
                .findAny().orElseThrow();

        return provider.getBy(query.contentId());
    }

    private Predicate<ContentProvider> isProviderFor(ContentType contentType) {
        return e -> e.provideType().equals(contentType);
    }

    public record Query(String contentId, ContentType contentType) {
    }
}
