package com.pancake.api.content.infra;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Singular;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Builder(builderClassName = "Builder")
@JsonNaming(SnakeCaseStrategy.class)
public record TmdbPage<T>(int page, int totalPages, int totalResults, @Singular List<T> results) {

    public Page<T> toPage() {
        return new PageImpl<>(results(), toPageable(), totalResults());
    }

    private Pageable toPageable() {
        final int PAGE_SIZE = 20;
        final int PAGE_START_NUMBER = 1;

        return PageRequest.of(page() - PAGE_START_NUMBER, PAGE_SIZE);
    }
}
