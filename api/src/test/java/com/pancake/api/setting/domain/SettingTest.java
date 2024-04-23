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
        var given = create(null);

        //when
        var actual = given.isEnabledAt(instant("2000-01-01T00:00:00Z"));

        //then
        assertThat(actual).isTrue();
    }

    @Test
    void 주어진_시간이_비활성화_날짜_이전이면_활성화_상태이다() {
        //given
        var given = create("2000-01-02T00:00:00Z");

        //when
        var actual = given.isEnabledAt(instant("2000-01-01T00:00:00Z"));

        //then
        assertThat(actual).isTrue();
    }

    @Test
    void 주어진_시간이_비활성화_날짜_이후면_비활성화_상태이다() {
        //given
        var given = create("2000-01-01T00:00:00Z");

        //when
        var actual = given.isEnabledAt(instant("2000-01-01T00:00:00Z"));

        //then
        assertThat(actual).isFalse();
    }

    private Setting create(String disableFrom) {
        return new Setting(NETFLIX, disableFrom(disableFrom));
    }

    private Instant instant(String datetime) {
        return Instant.parse(datetime);
    }

    private Instant disableFrom(String date) {
        if (date == null) {
            return null;
        }
        return instant(date);
    }

}