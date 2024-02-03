package com.pancake.api.content.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
        boolean watched = content.watch();

        //then
        assertAll(
                () -> assertThat(watched).isTrue(),
                () -> assertThat(content.isWatched()).isTrue()
        );
    }

    @DisplayName("컨텐츠에 url 추가한다")
    @Test
    void addUrl() {
        //given
        var content = createContent();

        //when
        content.addUrl("https://www.netflix.com/watch/999");

        //then
        assertThat(content.url()).isEqualTo("https://www.netflix.com/watch/999");
    }

    private Content createContent() {
        return new Content("테스트용 제목", "테스트용 설명", "https://occ.nflxso.net/api/0");
    }
}