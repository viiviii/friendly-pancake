package com.pancake.api.watch.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.pancake.api.watch.Builders.aWatchContent;
import static com.pancake.api.watch.Builders.aWatchOption;
import static org.assertj.core.api.Assertions.assertThat;

class WatchContentTest {

    @DisplayName("시청 옵션이 있으면 시청 가능하다")
    @Test
    void canWatchIsTrue() {
        //given
        var content = aWatchContent()
                .option(aWatchOption().build())
                .build();

        //when
        var actual = content.canWatch();

        //then
        assertThat(actual).isTrue();
    }

    @DisplayName("시청 옵션이 없는 컨텐츠는 시청 불가하다")
    @Test
    void canWatchIsFalse() {
        //given
        var content = aWatchContent()
                .clearOptions()
                .build();

        //then
        assertThat(content.canWatch()).isFalse();
    }
}