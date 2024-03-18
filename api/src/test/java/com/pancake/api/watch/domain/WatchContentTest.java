package com.pancake.api.watch.domain;

import org.junit.jupiter.api.Test;

import static com.pancake.api.watch.Builders.aWatchContent;
import static com.pancake.api.watch.Builders.aWatchOption;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class WatchContentTest {

    @Test
    void 시청옵션이_있으면_시청_가능하다() {
        //given
        var content = aWatchContent()
                .option(aWatchOption().build())
                .build();

        //when
        var actual = content.canWatch();

        //then
        assertThat(actual).isTrue();
    }

    @Test
    void 시청옵션이_없으면_시청_불가능하다() {
        //given
        var content = aWatchContent()
                .clearOptions()
                .build();

        //then
        assertThat(content.canWatch()).isFalse();
    }
}