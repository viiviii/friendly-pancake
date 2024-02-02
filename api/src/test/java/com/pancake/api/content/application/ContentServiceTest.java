package com.pancake.api.content.application;

import com.pancake.api.content.application.dto.AddWatchRequest;
import com.pancake.api.content.infra.ContentRepository;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ContentServiceTest {

    private final ContentRepository contentRepository = mock(ContentRepository.class);
    private final ContentService contentService = new ContentService(contentRepository);

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
        ThrowingCallable actual = () -> contentService.addWatch(anyLong(), watchRequest());

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

    private AddWatchRequest watchRequest() {
        return new AddWatchRequest("url");
    }
}