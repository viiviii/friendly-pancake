package com.pancake.api;

import com.pancake.api.content.infra.api.TmdbClient;
import com.pancake.api.content.infra.api.TmdbMovie;
import com.pancake.api.content.infra.api.TmdbPage;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.boot.test.context.TestComponent;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TestComponent
public class MemoryMovies implements TmdbClient {

    private final Map<String, TmdbMovie> map = new HashMap<>();

    void 존재한다(Item data) {
        map.put(data.id(), data.toTmdbMovie());
    }

    @Override
    public TmdbPage<TmdbMovie> searchMoviesBy(String query) {
        return TmdbPage.<TmdbMovie>builder()
                .page(1)
                .results(map.values().stream()
                        .filter(e -> e.title().equals(query))
                        .toList())
                .build();
    }

    @Override
    public TmdbMovie getMovieBy(String movieId) {
        return map.get(movieId);
    }

    @Builder(builderMethodName = "aItem")
    @Getter
    @Accessors(fluent = true)
    static final class Item {

        @Builder.Default
        private String id = "8392";

        @Builder.Default
        private String title = "이웃집 토토로";

        private TmdbMovie toTmdbMovie() {
            return aTmdbMovie()
                    .id(Integer.parseInt(id))
                    .title(title)
                    .build();
        }

        private TmdbMovie.TmdbMovieBuilder aTmdbMovie() {
            return TmdbMovie.builder()
                    .adult(false)
                    .backdropPath("/fxYazFVeOCHpHwu.jpg")
                    .genreIds(List.of(14, 16, 10751))
                    .id(8392)
                    .originalLanguage("ja")
                    .originalTitle("となりのトトロ")
                    .overview("1955년 일본의 아름다운 시골 마을, 상냥하고 의젓한 11살 사츠키와 장난꾸러기에 호기심 많은 4살의 메이...")
                    .popularity(0.601f)
                    .posterPath("/c9zCkL0rTkNQ1HB9c.jpg")
                    .releaseDate(LocalDate.of(2099, 12, 29))
                    .title("이웃집 토토로")
                    .video(false)
                    .voteAverage(8.073f)
                    .voteCount(7599);
        }
    }
}
