package com.pancake.api.content.domain;

import org.junit.jupiter.api.Test;

import static com.pancake.api.content.domain.Platform.DISNEY_PLUS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
class PlaybackTest {

    @Test
    void 생성_시_시청주소와_플랫폼이_일치하지_않으면_예외를_던진다() {
        assertThatThrownBy(() -> new Playback("https://www.netflix.com/watch/1", DISNEY_PLUS))
                .isInstanceOf(IllegalArgumentException.class);
    }

}