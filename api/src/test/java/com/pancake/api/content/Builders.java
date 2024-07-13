package com.pancake.api.content;

import com.pancake.api.content.application.ContentMetadata;
import com.pancake.api.content.application.ContentStreaming;
import com.pancake.api.content.domain.Platform;
import com.pancake.api.content.domain.Playback;
import com.pancake.api.content.infra.api.TmdbMovie;
import com.pancake.api.content.infra.api.TmdbPage;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

import static com.pancake.api.content.domain.Platform.NETFLIX;

public abstract class Builders {

    public static ContentMetadataBuilder aMetadata() {
        return contentMetadata()
                .id("99999")
                .contentType("movie")
                .title("테스트용 제목")
                .description("테스트용 설명")
                .imageUrl("테스트용 이미지 주소");
    }

    public static ContentStreamingBuilder aStreaming() {
        return contentStreaming()
                .url("https://www.netflix.com/watch/100000001");
    }

    public static PlaybackBuilder aPlayback() {
        return playback()
                .url("https://www.netflix.com/watch/100000001")
                .platform(NETFLIX);
    }

    public static <T> TmdbPage.Builder<T> aTmdbPage() {
        return TmdbPage.<T>builder()
                .page(1)
                .totalResults(1);
    }

    public static TmdbMovie.Builder aTmdbMovie() {
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

    @Builder(builderMethodName = "contentMetadata")
    private static ContentMetadata create(String id, String contentType, String title, String description, String imageUrl) {
        return new ContentMetadata(id, contentType, title, "original title", description, imageUrl, LocalDate.of(2099, 12, 31));
    }

    @Builder(builderMethodName = "contentStreaming")
    private static ContentStreaming create(String url) {
        return new ContentStreaming(url);
    }

    @Builder(builderMethodName = "playback")
    private static Playback create(String url, Platform platform) {
        return new Playback(url, platform);
    }
}