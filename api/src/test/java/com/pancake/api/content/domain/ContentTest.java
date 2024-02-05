package com.pancake.api.content.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

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

    @DisplayName("컨텐츠에 url 추가한다")
    @Test
    void addUrl() {
        //given
        var content = createContent();

        //when
        content.addUrl("https://www.netflix.com/watch/999");

        //then
        assertThat(content.getUrl()).isEqualTo("https://www.netflix.com/watch/999");
    }

    @DisplayName("시청 주소가 있는 컨텐츠는 시청 가능하다")
    @Test
    void canWatchIsTrue() {
        //given
        var content = createContent();
        content.addUrl("https://www.netflix.com/watch/999");

        //when
        var actual = content.canWatch();
        //then
        assertThat(actual).isTrue();
    }

    @DisplayName("시청 주소가 없는 컨텐츠는 시청 불가하다")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void canWatchIsFalse(String url) {
        //given
        var content = createContent();
        content.addUrl(url);

        //when
        var actual = content.canWatch();
        //then
        assertThat(actual).isFalse();
    }

    private Content createContent() {
        return new Content("테스트용 제목", "테스트용 설명", "https://occ.nflxso.net/api/0");
    }
}