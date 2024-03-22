package com.pancake.api.watch.application;

import com.pancake.api.content.domain.Platform;
import com.pancake.api.watch.domain.FindWatchOption;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.pancake.api.content.domain.Platform.DISNEY_PLUS;
import static com.pancake.api.content.domain.Platform.NETFLIX;
import static com.pancake.api.watch.Builders.aWatchOption;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest
class GetWatchUrlTest {

    private final FindWatchOption watchOption = mock(FindWatchOption.class);
    private final GetWatchUrl getWatchUrl = new GetWatchUrl(watchOption);

    @Test
    void 존재하지_않는_아이디로_조회_시_예외가_발생한다() {
        //given
        given(watchOption.findByContentIdAndPlatform(anyLong(), any(Platform.class)))
                .willReturn(Optional.empty());

        //when
        ThrowingCallable actual = () -> getWatchUrl.query(anyLong(), any(Platform.class));

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_시청_플랫폼으로_조회_시_예외가_발생한다() {
        //given
        given(watchOption.findByContentIdAndPlatform(anyLong(), eq(DISNEY_PLUS)))
                .willReturn(Optional.of(aWatchOption().platform(DISNEY_PLUS).build()));

        //when
        ThrowingCallable actual = () -> getWatchUrl.query(anyLong(), eq(NETFLIX));

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }
}