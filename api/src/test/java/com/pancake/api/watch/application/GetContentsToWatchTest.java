package com.pancake.api.watch.application;

import com.pancake.api.watch.domain.FindEnabledPlatforms;
import com.pancake.api.watch.domain.FindWatchContent;
import com.pancake.api.watch.domain.WatchContent;
import org.junit.jupiter.api.Test;

import static com.pancake.api.content.domain.Platform.NETFLIX;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SuppressWarnings("NonAsciiCharacters")
class GetContentsToWatchTest {

    private final FindWatchContent findWatchContent = mock(FindWatchContent.class);
    private final FindEnabledPlatforms findWatchSetting = mock(FindEnabledPlatforms.class);
    private final GetContentsToWatch getContentsToWatch = new GetContentsToWatch(findWatchContent, findWatchSetting);

    @Test
    void 설정한_플랫폼에서_시청_가능한_컨텐츠만_포함된다() {
        //given
        var content = mock(WatchContent.class);
        given(findWatchContent.findAll()).willReturn(of(content));
        given(findWatchSetting.findEnabledPlatforms()).willReturn(of(NETFLIX));
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
        given(findWatchContent.findAll()).willReturn(of(content, content, content));
        given(findWatchSetting.findEnabledPlatforms()).willReturn(of(NETFLIX));
        given(content.canWatchOnAny(of(NETFLIX))).willReturn(false);

        //when
        var actual = getContentsToWatch.query();

        //then
        assertThat(actual.getContents()).isEmpty();
    }
}