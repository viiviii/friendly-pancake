package com.pancake.api.content.application;

import com.pancake.api.content.domain.Content;
import com.pancake.api.content.domain.ContentRepository;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SuppressWarnings("NonAsciiCharacters")
class ContentServiceTest {

    private final ContentRepository contentRepository = mock(ContentRepository.class);
    private final ContentService contentService = new ContentService(contentRepository);

    @Test
    void 목록_조회_시_시청할_수_없는_컨텐츠는_제외된다() {
        //given
        var content = mock(Content.class);
        given(content.canWatch()).willReturn(false, true, true);
        given(contentRepository.findAll()).willReturn(List.of(content, content, content));

        //when
        var actual = contentService.getAllContents();

        //then
        assertThat(actual).hasSize(2);
    }

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