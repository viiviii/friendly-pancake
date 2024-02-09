package com.pancake.api.content.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ContentTest {

    @DisplayName("최초 생성 시 기본 상태는 [시청하지 않음]이다")
    @Test
    void create() {
        //when
        var content = createContent();

        //then
        assertThat(content.isWatched()).isFalse();
    }

    @DisplayName("컨텐츠를 시청 처리한다")
    @Test
    void watch() {
        //given
        var content = createContent();

        //when
        content.watch();

        //then
        assertThat(content.isWatched()).isTrue();
    }

    @DisplayName("컨텐츠에 재생 정보를 추가한다")
    @Test
    void addUrl() {
        //given
        var content = createContent();
        var playback = createPlayback();

        //when
        content.add(playback);

        //then
        assertThat(content.getPlaybacks())
                .singleElement()
                .isEqualTo(playback);
    }

    @DisplayName("재생 주소가 있는 컨텐츠는 시청 가능하다")
    @Test
    void canWatchIsTrue() {
        //given
        var content = createContent();
        content.add(createPlayback());

        //when
        var actual = content.canWatch();
        //then
        assertThat(actual).isTrue();
    }

    @DisplayName("재생 주소가 없는 컨텐츠는 시청 불가하다")
    @Test
    void canWatchIsFalse() {
        //given
        var content = createContent();

        //then
        assertThat(content.canWatch()).isFalse();
    }

    private Content createContent() {
        return new Content("테스트용 제목", "테스트용 설명", "https://occ.nflxso.net/api/0");
    }

    private Playback createPlayback() {
        var playbackUrl = new PlaybackUrl("https://www.netflix.com/watch/999");

        return new Playback(playbackUrl);
    }
}