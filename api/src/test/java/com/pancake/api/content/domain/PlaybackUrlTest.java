package com.pancake.api.content.domain;

import org.junit.jupiter.api.Test;

import static com.pancake.api.content.domain.Platform.DISNEY_PLUS;
import static com.pancake.api.content.domain.Platform.NETFLIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PlaybackUrlTest {

    @Test
    void isSatisfiedBy() {
        //given
        var url = create("https://www.netflix.com/watch/1");

        //then
        assertAll(
                () -> assertThat(url.isSatisfiedBy(NETFLIX)).isTrue(),
                () -> assertThat(url.isSatisfiedBy(DISNEY_PLUS)).isFalse()
        );
    }

    @Test
    void equality() {
        //given
        var url = create("https://www.disneyplus.com/video/1");
        var same = create("https://www.disneyplus.com/video/1");
        var different = create("https://www.disneyplus.com/video/2");

        //then
        assertThat(url)
                .isEqualTo(same).hasSameHashCodeAs(same)
                .isNotEqualTo(different).doesNotHaveSameHashCodeAs(different);
    }

    @Test
    void hasToString() {
        //given
        var url = create("https://www.disneyplus.com/video/1");

        //then
        assertThat(url).hasToString("https://www.disneyplus.com/video/1");
    }

    private PlaybackUrl create(String url) {
        return new PlaybackUrl(url);
    }
}