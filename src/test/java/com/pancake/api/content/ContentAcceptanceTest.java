package com.pancake.api.content;

import com.pancake.api.content.application.dto.ContentRequest;
import com.pancake.api.content.application.dto.ContentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.pancake.api.content.Fixtures.Netflix.PONYO;
import static com.pancake.api.content.Fixtures.Netflix.TOTORO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Sql("/database_cleanup.sql")
@SuppressWarnings("NonAsciiCharacters")
class ContentAcceptanceTest {

    @Autowired
    WebTestClient client;

    @Test
    void 컨텐츠를_등록할_수_있다() {
        //given
        var 등록할_컨텐츠_주소 = "https://www.netflix.com/watch/60023642?trackId=14234261";
        var 등록할_컨텐츠_이미지 = "https://occ-0-1360-2218.1.nflxso.net/dnm/api/v6/E8vDc";
        var 등록할_컨텐츠_제목 = "센과 치히로의 행방불명";
        var 등록할_컨텐츠_설명 = "마녀가 지배하는 신비한 세계에 발을 들여놓은 치히로. 마녀에게 거역하는 자는 동물로 변하게 되는데...";

        var 요청 = new ContentRequest(등록할_컨텐츠_주소, 등록할_컨텐츠_제목, 등록할_컨텐츠_이미지, 등록할_컨텐츠_설명);

        //when
        var 등록된_컨텐츠 = 컨텐츠를_등록한다(요청);

        //then
        assertThat(컨텐츠를_모두_조회한다()).containsExactly(등록된_컨텐츠);
    }

    @Test
    void 컨텐츠를_시청_처리_할_수_있다() {
        //given
        var 포뇨 = 포뇨_컨텐츠();

        //when
        컨텐츠를_시청_처리_한다(포뇨.getId());

        //then
        assertThat(컨텐츠를_모두_조회한다()).extracting("id", "watched")
                .contains(tuple(포뇨.getId(), true)); // TODO
    }

    @Test
    void 컨텐츠를_모두_조회할_수_있다() {
        //given
        var 시청하지_않은_토토로 = 토토로_컨텐츠();
        var 시청한_포뇨 = 시청한(포뇨_컨텐츠());

        //when
        var 모든_컨텐츠_목록 = 컨텐츠를_모두_조회한다();

        //then
        assertThat(모든_컨텐츠_목록).extracting("id")
                .containsExactly(시청하지_않은_토토로.getId(), 시청한_포뇨.getId()); // TODO
    }

    private ContentResponse 토토로_컨텐츠() {
        return 컨텐츠를_등록한다(TOTORO.REQUEST);
    }

    private ContentResponse 포뇨_컨텐츠() {
        return 컨텐츠를_등록한다(PONYO.REQUEST);
    }

    private ContentResponse 시청한(ContentResponse content) {
        컨텐츠를_시청_처리_한다(content.getId());

        return content;
    }

    private ContentResponse 컨텐츠를_등록한다(ContentRequest request) {
        return post("/api/contents", request, ContentResponse.class);
    }

    private void 컨텐츠를_시청_처리_한다(long id) {
        patch("/api/contents/{id}/watch", id, Boolean.class);
    }

    private ContentResponse[] 컨텐츠를_모두_조회한다() {
        return get("/api/contents", ContentResponse[].class);
    }

    private <T> T get(String path, Class<T> expectBodyType) {
        return client.get().uri(path)
                .exchange()
                .expectBody(expectBodyType)
                .returnResult()
                .getResponseBody();
    }

    private <T> T post(String path, Object body, Class<T> expectBodyType) {
        return client.post().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectBody(expectBodyType)
                .returnResult()
                .getResponseBody();
    }

    private <T> T patch(String path, Object uriVariable, Class<T> expectBodyType) {
        return client.patch().uri(path, uriVariable)
                .exchange()
                .expectBody(expectBodyType)
                .returnResult()
                .getResponseBody();
    }
}
