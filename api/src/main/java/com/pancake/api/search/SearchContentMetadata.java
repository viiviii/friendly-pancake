package com.pancake.api.search;

import com.pancake.api.content.application.ContentMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchContentMetadata {

    private final FindContentMetadata metadata;

    public Result queryBy(String title) {
        final var find = metadata.findAllByTitle(title);

        return new Result(find.hasNext(), find.getContent());
    }

    public record Result(boolean hasNext, List<ContentMetadata> contents) {

        public String getContentSource() {
            return "TMDB"; // TODO
        }
    }
}