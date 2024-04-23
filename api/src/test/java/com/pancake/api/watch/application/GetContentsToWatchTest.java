package com.pancake.api.watch.application;

import com.pancake.api.watch.domain.FindEnabledPlatforms;
import com.pancake.api.watch.domain.FindWatchContent;
import com.pancake.api.watch.domain.WatchContent;
import org.junit.jupiter.api.Test;

import java.time.Clock;

import static com.pancake.api.content.domain.Platform.NETFLIX;
import static java.time.Instant.parse;
import static java.time.ZoneOffset.UTC;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@SuppressWarnings("NonAsciiCharacters")
class GetContentsToWatchTest {

    private final FindWatchContent findWatchContent = mock(FindWatchContent.class);
    private final FindEnabledPlatforms findEnabledPlatforms = mock(FindEnabledPlatforms.class);
    private final Clock clock = Clock.fixed(parse("2000-01-01T00:00:00Z"), UTC);
    private final GetContentsToWatch getContentsToWatch = new GetContentsToWatch(findWatchContent, findEnabledPlatforms, clock);

    @Test
    void 현재_시간의_5분_후를_기준으로_활성화된_플랫폼을_찾는다() {
        //given
        var expected = clock.instant().plus(5, MINUTES);

        //when
        getContentsToWatch.query();

        //then
        then(findEnabledPlatforms).should().findEnabledPlatformsAt(expected);
    }

    @Test
    void 설정한_플랫폼에서_시청_가능한_컨텐츠만_포함된다() {
        //given
        var content = mock(WatchContent.class);
        given(findEnabledPlatforms.findEnabledPlatformsAt(any())).willReturn(of(NETFLIX));
        given(findWatchContent.findAll()).willReturn(of(content));
        given(content.canWatchOnAny(of(NETFLIX))).willReturn(true);

        //when
        var actual = getContentsToWatch.query();

        //then
        assertThat(actual.getContents()).isNotEmpty();
    }

    @Test
    void 설정한_플랫폼에서_시청_불가능한_컨텐츠는_제외된다() {
        //given
        var content = mock(WatchContent.class);
        given(findEnabledPlatforms.findEnabledPlatformsAt(any())).willReturn(of(NETFLIX));
        given(findWatchContent.findAll()).willReturn(of(content));
        given(content.canWatchOnAny(of(NETFLIX))).willReturn(false);

        //when
        var actual = getContentsToWatch.query();

        //then
        assertThat(actual.getContents()).isEmpty();
    }
}