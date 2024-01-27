package com.pancake.api.content.application;

import com.pancake.api.content.application.dto.ContentResponse;
import com.pancake.api.content.helper.ContentRequestBuilders;
import com.pancake.api.content.helper.ContentRequestBuilders.ContentRequestBuilder;
import com.pancake.api.content.infra.MemoryContentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.pancake.api.content.helper.ContentRequestBuilders.aRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ContentServiceTest {
    private static final long NOT_EXISTS_ID = Long.MAX_VALUE;

    private final ContentService contentService = new ContentService(new MemoryContentRepository());

    @DisplayName("컨텐츠를 저장한다")
    @Test
    void save() {
        //given
        var request = ContentRequestBuilders.builder()
                .title("이웃집 토토로")
                .description("일본의 한 시골 마을에서 여름을 보내게 된다")
                .imageUrl("https://occ.nflxso.net/api/999")
                .build();

        //when
        var actual = contentService.save(request);

        //then
        assertAll(
                () -> assertThat(actual.getTitle()).isEqualTo("이웃집 토토로"),
                () -> assertThat(actual.getDescription()).isEqualTo("일본의 한 시골 마을에서 여름을 보내게 된다"),
                () -> assertThat(actual.getImageUrl()).isEqualTo("https://occ.nflxso.net/api/999")
        );
    }

    @DisplayName("컨텐츠를 모두 조회한다")
    @Test
    void getAllContents() {
        //given
        var ironMan = save(aRequest().title("아이언맨"));
        var thor = save(aRequest().title("토르"));

        //when
        var actual = contentService.getAllContents();

        //then
        assertThat(actual).containsExactly(ironMan, thor);
    }

    @DisplayName("컨텐츠를 아이디로 조회한다")
    @Test
    void getContent() {
        //given
        var content = save(aRequest());

        //when
        var actual = contentService.getContent(content.getId());

        //then
        assertThat(actual).isEqualTo(content);
    }

    @DisplayName("컨텐츠를 시청 처리한다")
    @Test
    void watch() {
        //given
        var contentId = save(aRequest()).getId();

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

    private ContentResponse save(ContentRequestBuilder request) {
        return contentService.save(request.build());
    }
}