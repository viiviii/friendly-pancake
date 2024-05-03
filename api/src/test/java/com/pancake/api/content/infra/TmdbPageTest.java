package com.pancake.api.content.infra;

import org.junit.jupiter.api.Test;

import static com.pancake.api.content.Builders.aTmdbPage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
class TmdbPageTest {
    
    @Test
    void 다음_페이지가_없을_때() {
        //given
        var page = aTmdbPage()
                .page(1)
                .totalPages(1)
                .totalResults(20)
                .build();

        //when
        var actual = page.toPage();

        //then
        assertAll(
                () -> assertThat(actual.getNumber()).as("api 페이지는 1부터, 스프링은 0부터 시작한다").isZero(),
                () -> assertThat(actual.getTotalPages()).isEqualTo(page.totalPages()),
                () -> assertThat(actual.getTotalElements()).isEqualTo(page.totalResults()),
                () -> assertThat(actual.hasNext()).isFalse()
        );
    }

    @Test
    void 다음_페이지가_있을_때() {
        //given
        var page = aTmdbPage()
                .page(3)
                .totalPages(9)
                .totalResults(176)
                .build();

        //when
        var actual = page.toPage();

        //then
        assertAll(
                () -> assertThat(actual.getNumber()).isEqualTo(2),
                () -> assertThat(actual.getTotalPages()).isEqualTo(page.totalPages()),
                () -> assertThat(actual.getTotalElements()).isEqualTo(page.totalResults()),
                () -> assertThat(actual.hasNext()).isTrue()
        );
    }
}