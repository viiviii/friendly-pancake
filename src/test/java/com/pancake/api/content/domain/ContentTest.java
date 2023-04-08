package com.pancake.api.content.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ContentTest {

    @DisplayName("최초 생성 시 기본 상태는 [시청하지 않음]이다")
    @Test
    void create() {
        //when
        var content = new Content("https://www.netflix.com/watch/60032294?trackId=254245392", "이웃집 토토로");

        //then
        assertThat(content.isWatched()).isFalse();
    }


    @DisplayName("컨텐츠를 시청 처리한다")
    @Test
    void watch() {
        //given
        var content = new Content("https://www.netflix.com/watch/60032294?trackId=254245392", "이웃집 토토로");

        //when
        boolean watched = content.watch();

        //then
        assertThat(watched).isTrue();
    }

    @DisplayName("컨텐츠의 시청 상태를 반환한다")
    @Test
    void isWatched() {
        //given
        var content = new Content(null, "https://www.netflix.com/watch/60032294?trackId=254245392", "이웃집 토토로", true);

        //then
        assertThat(content.isWatched()).isTrue();
    }

}