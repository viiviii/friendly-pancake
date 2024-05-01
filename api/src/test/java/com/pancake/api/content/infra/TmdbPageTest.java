package com.pancake.api.content.infra;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.util.List;

import static com.pancake.api.content.Builders.aTmdbPage;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JsonTest
@SuppressWarnings("NonAsciiCharacters")
class TmdbPageTest {

    @Autowired
    JacksonTester<TmdbPage<Object>> json;

    @Test
    void serialize() throws Exception {
        //given
        var page = aTmdbPage()
                .page(1)
                .totalPages(1)
                .totalResults(20)
                .results(List.of(1, 2))
                .build();

        //when
        var actual = json.write(page);

        //then
        assertThat(actual)
                .hasJsonPathNumberValue("@.page")
                .hasJsonPathNumberValue("@.total_pages")
                .hasJsonPathNumberValue("@.total_results")
                .hasJsonPathArrayValue("@.results");
    }

    @Test
    void deserialize() throws Exception {
        //given
        var content = """
                {"page":1,"results":[], "total_pages":9,"total_results":176}""";
        //when
        var actual = json.parse(content);

        assertThat(actual.getObject()).isEqualTo(aTmdbPage()
                .page(1)
                .results(emptyList())
                .totalPages(9)
                .totalResults(176)
                .build());
    }

    @Nested
    class 스프링_페이지_클래스로_변경 {

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
}