package com.pancake.api.content.application;

import com.pancake.api.content.domain.Content;
import com.pancake.api.content.infra.MemoryContentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.pancake.api.content.Fixtures.NOT_EXISTS_ID;
import static com.pancake.api.content.Fixtures.Netflix.TOTORO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ContentServiceTest {

    private final ContentService contentService = new ContentService(new MemoryContentRepository());

    @DisplayName("컨텐츠를 저장한다")
    @Test
    void save() {
        //given
        var request = TOTORO.REQUEST;
        //when
        var actual = contentService.save(request);

        //then
        assertAll(
                () -> assertThat(actual.url()).isEqualTo(request.getUrl()),
                () -> assertThat(actual.title()).isEqualTo(request.getTitle()),
                () -> assertThat(actual.description()).isEqualTo(request.getDescription()),
                () -> assertThat(actual.imageUrl()).isEqualTo(request.getImageUrl())
        );
    }

    @DisplayName("컨텐츠를 모두 조회한다")
    @Test
    void getAllContents() {
        //given
        var unwatchedContent = existContent();
        var watchedContent = existContent();
        watchedContent.watch();

        //when
        var actual = contentService.getAllContents();

        //then
        assertThat(actual).containsExactly(unwatchedContent, watchedContent);
    }

    @DisplayName("컨텐츠를 아이디로 조회한다")
    @Test
    void getContent() {
        //given
        var content = existContent();

        //when
        var actual = contentService.getContent(content.id());

        //then
        assertThat(actual).isEqualTo(content);
    }

    @DisplayName("컨텐츠를 시청 처리한다")
    @Test
    void watch() {
        //given
        var contentId = existContent().id();

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

    private Content existContent() {
        return contentService.save(TOTORO.REQUEST);
    }
}