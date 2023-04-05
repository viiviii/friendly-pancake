package com.pancake.api.content;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ContentServiceTest {

    private final ContentService contentService = new ContentService();

    @Test
    void save() {
        //given
        var request = new ContentRequest("https://www.netflix.com/watch/60023642?trackId=14234261", "센과 치히로의 행방불명");

        //when
        var actual = contentService.save(request);

        //then
        assertAll(
                () -> assertThat(actual.id()).isPositive(),
                () -> assertThat(actual.url()).isEqualTo(request.getUrl()),
                () -> assertThat(actual.title()).isEqualTo(request.getTitle())
        );
    }

}