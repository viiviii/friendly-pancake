package com.pancake.api.content.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class PlatformTest {

    @ParameterizedTest
    @EnumSource
    void 기본주소는_슬래시로_끝난다(Platform platform) {
        assertThat(platform.baseUrl()).endsWith("/");
    }
}