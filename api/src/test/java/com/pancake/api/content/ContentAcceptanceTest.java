package com.pancake.api.content;

import com.pancake.api.content.application.dto.ContentRequest;
import com.pancake.api.content.application.dto.ContentResponse;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = "spring.flyway.clean-disabled=false")
@SuppressWarnings("NonAsciiCharacters")
class ContentAcceptanceTest {

    @Autowired
    WebTestClient client;

    @AfterEach
    void cleanUp(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @DisplayName("등록된 컨텐츠가 있다")
    @Nested
    class Given {
        String 컨텐츠_주소 = "https://www.netflix.com/watch/70106454?trackId=255824129";
        String 컨텐츠_제목 = "벼랑 위의 포뇨";
        Long 컨텐츠_아이디;

        @BeforeEach
        void setUp() {
            var 요청 = new ContentRequest(컨텐츠_주소, 컨텐츠_제목,
                    "https://occ-0-1360-2218.1.nflxso.net/dnm/api/v6/E8vDc",
                    """
                            따분한 바다 생활이 싫어 가출한 물고기 공주 포뇨. 벼랑 위에 사는 인간 꼬마 소스케를 만나 친구가 된다.
                            온 바다가 들썩들썩 포뇨를 찾아 나서지만, 이 고집불통 물고기의 소원은 오직 하나. 포뇨도 소스케처럼 인간이 될 거야!""");
            var 등록된_컨텐츠 = 컨텐츠를_등록한다(요청);
            컨텐츠_아이디 = 등록된_컨텐츠.getId();
        }

        @Test
        void 컨텐츠를_모두_조회할_수_있다() {
            //when
            var 모든_컨텐츠_목록 = 컨텐츠를_모두_조회한다();

            //then
            assertThat(모든_컨텐츠_목록)
                    .extracting("title")
                    .containsExactly(컨텐츠_제목);
        }

        @Test
        void 컨텐츠_주소로_이동할_수_있다() {
            //when
            var 이동된_위치 = 컨텐츠_주소로_이동한다(컨텐츠_아이디);

            //then
            assertThat(이동된_위치).hasToString(컨텐츠_주소);
        }

        // TODO: 이거 위치 이상하네 지금 시청한 컨텐츠 목록을 app에서 처리해서 인듯
        @Test
        void 컨텐츠를_시청_처리_할_수_있다() {
            //when
            컨텐츠를_시청_처리_한다(컨텐츠_아이디);

            //then
            assertThat(컨텐츠를_모두_조회한다()) // 트랜잭션 동작 확인을 위해 새로 조회한 값을 사용
                    .extracting("id", "watched")
                    .contains(tuple(컨텐츠_아이디, true));
        }
    }

    private ContentResponse 컨텐츠를_등록한다(ContentRequest request) {
        return client.post().uri("/api/contents")
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectBody(ContentResponse.class)
                .returnResult()
                .getResponseBody();
    }

    private void 컨텐츠를_시청_처리_한다(long id) {
        client.patch().uri("/api/contents/{id}/watched", id).exchange();
    }

    private ContentResponse[] 컨텐츠를_모두_조회한다() {
        return client.get().uri("/api/contents")
                .exchange()
                .expectBody(ContentResponse[].class)
                .returnResult()
                .getResponseBody();
    }

    private URI 컨텐츠_주소로_이동한다(long id) {
        return client.get().uri("/api/contents/{id}", id)
                .exchange()
                .returnResult(Void.class)
                .getResponseHeaders()
                .getLocation();
    }
}
