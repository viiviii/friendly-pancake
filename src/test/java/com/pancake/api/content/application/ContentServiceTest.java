package com.pancake.api.content.application;

import com.pancake.api.content.NetflixConstant;
import com.pancake.api.content.application.dto.ContentRequest;
import com.pancake.api.content.domain.Content;
import com.pancake.api.content.infra.MemoryContentRepository;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.pancake.api.content.NetflixConstant.PONYO;
import static com.pancake.api.content.NetflixConstant.TOTORO;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.data.Index.atIndex;
import static org.junit.jupiter.api.Assertions.assertAll;

class ContentServiceTest {

    private final ContentService contentService = new ContentService(new MemoryContentRepository());

    private final Condition<Content> watchedContent = new Condition<>(Content::isWatched, "content is watched");
    private final Condition<Content> unwatchedContent = not(watchedContent);

    private final long NOT_EXISTS_ID = Long.MAX_VALUE;


    @DisplayName("컨텐츠를 저장한다")
    @Test
    void save() {
        //given
        var request = new ContentRequest(TOTORO.URL, TOTORO.TITLE);

        //when
        var actual = contentService.save(request);

        //then
        assertAll(
                () -> assertThat(actual.url()).isEqualTo(TOTORO.URL),
                () -> assertThat(actual.title()).isEqualTo(TOTORO.TITLE)
        );
    }

    @DisplayName("컨텐츠를 시청 처리한다")
    @Test
    void watch() {
        //given
        var contentId = existUnwatchedContent().id();

        //when
        var watched = contentService.watch(contentId);

        //then
        assertThat(watched).isTrue();
    }

    @DisplayName("존재하지 않는 컨텐츠를 시청 처리 시 예외가 발생한다")
    @Test
    void watchThrownExceptionWhenContentNotExist() {
        assertThatThrownBy(() -> contentService.watch(NOT_EXISTS_ID))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("컨텐츠를 모두 조회한다")
    @Test
    void getAllContents() {
        //given
        existWatchedContent();
        existUnwatchedContent();

        //when
        var actual = contentService.getAllContents();

        //then
        assertThat(actual)
                .hasSize(2)
                .has(watchedContent, atIndex(0))
                .has(unwatchedContent, atIndex(1));
    }

    @DisplayName("시청하지 않은 컨텐츠를 모두 조회한다")
    @Test
    void getUnwatchedContents() {
        //given
        existWatchedContent();
        existUnwatchedContent();

        //when
        var actual = contentService.getUnwatchedContents();

        //then
        assertThat(actual).hasSize(1).are(unwatchedContent);
    }

    @DisplayName("시청한 컨텐츠를 모두 조회한다")
    @Test
    void getWatchedContents() {
        //given
        existWatchedContent();
        existUnwatchedContent();

        //when
        var actual = contentService.getWatchedContents();

        //then
        assertThat(actual).hasSize(1).are(watchedContent);
    }

    private void existWatchedContent() {
        saveContent(TOTORO).watch();
    }

    private Content existUnwatchedContent() {
        return saveContent(PONYO);
    }

    private Content saveContent(NetflixConstant netflix) {
        return contentService.save(new ContentRequest(netflix.URL, netflix.TITLE));
    }
}