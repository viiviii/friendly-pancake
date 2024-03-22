package com.pancake.api.content.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
class UrlTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void 생성_시_주소가_빈_값이면_예외를_던진다(String given) {
        assertThatThrownBy(() -> create(given)).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            " https://www.netflix.com/watch/0000000",
            "https://www.netflix.com/watch/0000000 ",
            "https://www.netflix.com/watch/ 0000000",
    })
    void 생성_시_주소에_공백이_있으면_예외를_던진다() {
        //given
        var given = "https://www.netflix.com/watch/ 0000000";

        //then
        assertThatThrownBy(() -> create(given)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성_시_상대_주소이면_예외를_던진다() {
        //given
        var given = "/watch/0000000";

        //then
        assertThatThrownBy(() -> create(given)).isInstanceOf(IllegalArgumentException.class);
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

    private Url create(String value) {
        return new Url(value);
    }
}