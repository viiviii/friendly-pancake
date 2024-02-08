package com.pancake.api.content.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PlaybackUrlTest {

    @DisplayName("주소가 비어있는 경우 예외를 던진다")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void createThrownExceptionWhenEmptyString(String url) {
        //then
        assertThatThrownBy(() -> create(url))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주소에 공백이 포함된 경우 예외를 던진다")
    @Test
    void createThrowExceptionWhenHasSpace() {
        //given
        var url = "https://www.netflix.com/watch/ 0000000";

        //then
        assertThatThrownBy(() -> create(url))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상대 주소인 경우 예외를 던진다")
    @Test
    void createThrowExceptionWhenRelativeUrl() {
        //given
        var url = "/watch/0000000";

        //then
        assertThatThrownBy(() -> create(url))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private PlaybackUrl create(String value) {
        return new PlaybackUrl(value);
    }
}