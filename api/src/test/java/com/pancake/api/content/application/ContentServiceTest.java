package com.pancake.api.content.application;

import com.pancake.api.content.domain.ContentRepository;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SuppressWarnings("NonAsciiCharacters")
class ContentServiceTest {

    private final ContentRepository contentRepository = mock(ContentRepository.class);
    private final ContentService contentService = new ContentService(contentRepository);

    @Test
    void 존재하지_않는_컨텐츠를_시청_처리_시_예외가_발생한다() {
        //given
        given(contentRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        ThrowingCallable actual = () -> contentService.watch(anyLong());

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }
}