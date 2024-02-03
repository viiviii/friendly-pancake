package com.pancake.api.content;

import com.pancake.api.content.api.ContentResponse;
import com.pancake.api.content.application.AddWatchCommand;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec.ResponseSpecConsumer;

import java.util.List;

import static com.pancake.api.content.application.SaveContentCommandBuilders.aSaveContentCommand;
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

    @Test
    void 사용자가_컨텐츠를_시청한다() {
        //준비
        var 원하는_컨텐츠 = 등록된_컨텐츠가_있다().getId();
        컨텐츠에_시청주소를_추가한다(원하는_컨텐츠, "https://www.netflix.com/watch/70106454");

        //목표
        var 결과 = 컨텐츠를_시청한다(원하는_컨텐츠);

        //결과
        결과.expectAll(
                컨텐츠를_시청할_수_있는_주소로_이동된다("https://www.netflix.com/watch/70106454")
        );
    }

    private ResponseSpecConsumer 컨텐츠를_시청할_수_있는_주소로_이동된다(String url) {
        return spec -> spec.expectHeader().location(url);
    }

    private ResponseSpec 컨텐츠를_시청한다(long id) {
        return client.get().uri("/api/contents/{id}", id).exchange();
    }

    private ContentResponse 등록된_컨텐츠가_있다() {
        var request = aSaveContentCommand().build();

        return client.post().uri("/api/contents")
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(ContentResponse.class)
                .returnResult().getResponseBody();
    }

    private void 컨텐츠에_시청주소를_추가한다(long contentId, String url) {
        var request = new AddWatchCommand(url);
        client.post().uri("/api/contents/{id}/watch", contentId)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Void.class);
    }

    // TODO: 지금 쓸데 없죠
    private List<ContentResponse> 조회된_컨텐츠_목록이_있다() {
        return client.get().uri("/api/contents")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(ContentResponse.class)
                .returnResult()
                .getResponseBody();
    }
}
