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

    public ContentMetadata queryBy(String contentId, String contentType) {
        final var provider = providers.stream()
                .filter(isProviderFor(contentType))
                .findAny().orElseThrow();

        return provider.getBy(contentId);
    }

    private Predicate<ContentProvider> isProviderFor(String contentType) {
        return e -> e.provideType().equals(contentType);
    }
}
