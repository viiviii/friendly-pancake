package com.pancake.api.watch.application;

import com.pancake.api.watch.domain.WatchContent;
import com.pancake.api.watch.domain.WatchContentRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SuppressWarnings("NonAsciiCharacters")
class GetContentsToWatchTest {


    private final WatchContentRepository watchContentRepository = mock(WatchContentRepository.class);
    private final GetContentsToWatch getContentsToWatch = new GetContentsToWatch(watchContentRepository);

    @Test
    void 시청할_수_없는_컨텐츠는_제외된다() {
        //given
        var watch = mock(WatchContent.class);
        given(watch.canWatch()).willReturn(false, true, true);
        given(watchContentRepository.findAll()).willReturn(List.of(watch, watch, watch));

        //when
        var actual = getContentsToWatch.query();

        //then
        assertThat(actual).hasSize(2);
    }
}