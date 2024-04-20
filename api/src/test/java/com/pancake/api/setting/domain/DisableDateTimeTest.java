package com.pancake.api.setting.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
class DisableDateTimeTest {

    @Test
    void 항상_UTC_기준으로_값을_저장한다() {
        //when
        var actual = create("2024-04-01T00:00+09:00[Asia/Seoul]");

        //then
        assertThat(actual).hasToString("2024-03-31T15:00Z");
    }

    @Test
    void 주어진_시간_이후이다() {
        //given
        var actual = create("2024-04-02T15:00Z");

        //then
        assertAll(
                () -> assertThat(actual.isAfter(zoned("2024-04-02T00:00Z"))).isTrue(),
                () -> assertThat(actual.isAfter(zoned("2024-04-01T11:59+09:00[Asia/Seoul]"))).isTrue()
        );
    }

    @Test
    void 주어진_시간_이후가_아니다() {
        //given
        var actual = create("2024-04-01T15:00Z");

        //then
        assertAll(
                () -> assertThat(actual.isAfter(zoned("2024-04-01T00:00Z"))).isTrue(),
                () -> assertThat(actual.isAfter(zoned("2024-04-01T11:59+09:00[Asia/Seoul]"))).isTrue()
        );
    }

    @Test
    void hasToString() {
        //given
        var given = zoned("2000-01-01T00:00Z");

        //when
        var actual = create("2000-01-01T00:00Z").toString();


        //then
        assertThat(actual).isEqualTo(given.toString());
    }

    private DisableDateTime create(String value) {
        return new DisableDateTime(zoned(value));
    }

    private ZonedDateTime zoned(String value) {
        return ZonedDateTime.parse(value);
    }

    @Nested
    class ZonedDateTimeExampleTest {

        @Test
        void 두_시간은_동일하다() {
            //given
            var UTC_시간 = zoned("2000-01-01T00:00Z");
            var 서울_시간 = zoned("2000-01-01T09:00+09:00[Asia/Seoul]");

            //then
            assertThat(UTC_시간).isEqualTo(서울_시간);
        }

        @Test
        void systemUTC_사용_시_UTC_기준으로_시간을_얻을_수_있다() {
            //given
            var utc = ZonedDateTime.now(Clock.systemUTC());

            //when
            var actual = utc.getOffset();

            //then
            assertAll(
                    () -> assertThat(actual.getTotalSeconds()).isZero(),
                    () -> assertThat(actual.getId()).hasToString("Z")
            );
        }

        @Test
        void 특정_시간대를_UTC_시간으로_변경한다() {
            //given
            var seoul = zoned("2000-01-01T09:00+09:00[Asia/Seoul]");

            //when
            var actual = seoul.withZoneSameInstant(ZoneOffset.UTC);

            //then
            assertThat(actual).isEqualTo(seoul)
                    .hasToString("2000-01-01T00:00Z");
        }
    }
}
