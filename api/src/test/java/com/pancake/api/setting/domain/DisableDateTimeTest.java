package com.pancake.api.setting.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class DisableDateTimeTest {

    @Test
    void 주어진_시간_이후이다() {
        //given
        var actual = create("2024-04-02T15:00:00Z");

        //then
        assertThat(actual.isAfter(instant("2024-04-02T00:00:00Z"))).isTrue();
    }

    @Test
    void 주어진_시간_이후가_아니다() {
        //given
        var actual = create("2024-04-01T15:00:00Z");

        //then
        assertThat(actual.isAfter(instant("2024-04-01T00:00:00Z"))).isTrue();
    }

    @Test
    void hasToString() {
        //given
        var given = instant("2000-01-01T00:00:00Z");

        //when
        var actual = create("2000-01-01T00:00:00Z").toString();


        //then
        assertThat(actual).isEqualTo(given.toString());
    }

    @Test
    void equality() {
        //given
        var given = create("2000-01-01T00:00:00Z");
        var same = create("2000-01-01T00:00:00Z");
        var different = create("2099-12-31T12:59:00Z");

        //then
        assertThat(given)
                .isEqualTo(same).hasSameHashCodeAs(same)
                .isNotEqualTo(different).doesNotHaveSameHashCodeAs(different);
    }

    private DisableDateTime create(String value) {
        return new DisableDateTime(instant(value));
    }

    private Instant instant(String value) {
        return Instant.parse(value);
    }

    @Nested
    class InstantExampleTest {

        @Test
        void UTC_시간을_특정_시간대로_변경한다() {
            //given
            var instant = Instant.parse("2000-01-01T00:00:00Z");

            //when
            var actual = instant.atZone(ZoneId.of("Asia/Seoul"));

            //then
            assertThat(actual).hasToString("2000-01-01T09:00+09:00[Asia/Seoul]");
        }
    }
}
