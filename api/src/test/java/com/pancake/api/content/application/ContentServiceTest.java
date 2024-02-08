package com.pancake.api.content.application;

import com.pancake.api.content.domain.Content;
import com.pancake.api.content.domain.ContentRepository;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.pancake.api.content.application.Builders.aWatchToAdd;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ContentServiceTest {

    private final ContentRepository contentRepository = mock(ContentRepository.class);
    private final ContentService contentService = new ContentService(contentRepository);

    @DisplayName("컨텐츠 목록 조회 시 시청할 수 없는 컨텐츠는 제외된다")
    @Test
    void getAllContentsExcludesUnwatchableContent() {
        //given
        var content = mock(Content.class);
        given(content.canWatch()).willReturn(false, true, true);
        given(contentRepository.findAll()).willReturn(List.of(content, content, content));

        //when
        var actual = contentService.getAllContents();

        //then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("존재하지 않는 컨텐츠 조회 시 예외가 발생한다")
    @Test
    void getThrownExceptionWhenContentNotExist() {
        //given
        given(contentRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        ThrowingCallable actual = () -> contentService.getContent(anyLong());

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 컨텐츠에 시청 주소 추가 시 예외가 발생한다")
    @Test
    void addWatchThrownExceptionWhenContentNotExist() {
        //given
        given(contentRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        ThrowingCallable actual = () -> contentService.addWatch(anyLong(), aWatchToAdd().build());

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 컨텐츠를 시청 처리 시 예외가 발생한다")
    @Test
    void watchThrownExceptionWhenContentNotExist() {
        //given
        given(contentRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        ThrowingCallable actual = () -> contentService.watch(anyLong());

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }
}