package com.pancake.api.watch.domain;

import org.junit.jupiter.api.Test;

import static com.pancake.api.content.domain.Platform.DISNEY_PLUS;
import static com.pancake.api.content.domain.Platform.NETFLIX;
import static com.pancake.api.watch.Builders.aWatchContent;
import static com.pancake.api.watch.Builders.aWatchOption;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class WatchContentTest {

    @Test
    void 주어진_플랫폼_중_하나에서라도_컨텐츠를_시청할_수_있다() {
        //given
        var content = aWatchContent()
                .option(aWatchOption().platform(NETFLIX).build())
                .build();

        //when
        var actual = content.canWatchOnAny(of(NETFLIX));

        //then
        assertThat(actual).isTrue();
    }

    @Test
    void 주어진_플랫폼_중_어디에서도_컨텐츠를_시청할_수_없다() {
        //given
        var content = aWatchContent()
                .option(aWatchOption().platform(DISNEY_PLUS).build())
                .build();

        //when
        var actual = content.canWatchOnAny(of(NETFLIX));

        //then
        assertThat(actual).isFalse();
    }
}