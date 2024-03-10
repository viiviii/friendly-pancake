package com.pancake.api.content.application;

import com.pancake.api.content.domain.Platform;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.pancake.api.content.application.Builders.aPlaybackToAdd;
import static com.pancake.api.content.domain.Platform.DISNEY_PLUS;
import static com.pancake.api.content.domain.Platform.NETFLIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
class PlatformMapperTest {

    private final PlatformMapper mapper = new PlatformMapper();

    @Test
    void 스트리밍_정보를_플랫폼으로_매핑할_수_없으면_예외를_던진다() {
        //given
        var streaming = create("https://invalid.some.site/view/1");

        //when
        ThrowingCallable actual = () -> mapper.mapFrom(streaming);

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource("provideGivenAndExpected")
    void 스트리밍_정보로_플랫폼을_매핑한다(String given, Platform expected) {
        //given
        var streaming = create(given);

        //when
        var actual = mapper.mapFrom(streaming);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideGivenAndExpected() {
        return Stream.of(
                Arguments.of("https://www.netflix.com/watch/1", NETFLIX),
                Arguments.of("https://www.disneyplus.com/video/1", DISNEY_PLUS)
        );
    }

    private static AddPlaybackCommand create(String url) {
        return aPlaybackToAdd().url(url).build();
    }
}