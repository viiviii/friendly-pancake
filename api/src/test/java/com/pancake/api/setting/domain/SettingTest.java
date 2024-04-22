package com.pancake.api.setting.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.pancake.api.content.domain.Platform.NETFLIX;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class SettingTest {

    @Test
    void 비활성화_날짜가_지정되지_않았으면_항상_활성화_상태이다() {
        //given
        var given = setting(null);

        //when
        var actual = given.isEnabled();

        //then
        assertThat(actual).isTrue();
    }

    @Test
    void 현재_시간이_비활성화_날짜_이전이면_활성화_상태이다() {
        //given
        var given = setting("2099-12-31T00:00:00Z");

        //when
        var actual = given.isEnabled();

        //then
        assertThat(actual).isTrue();
    }

    @Test
    void 현재_시간이_비활성화_날짜_이후면_비활성화_상태이다() {
        //given
        var given = setting("1999-12-31T00:00:00Z");

        //when
        var actual = given.isEnabled();

        //then
        assertThat(actual).isFalse();
    }

    private Setting setting(String disableFrom) {
        return new Setting(NETFLIX, disableFrom(disableFrom));
    }

    private Instant disableFrom(String date) {
        if (date == null) {
            return null;
        }
        return Instant.parse(date);
    }

}