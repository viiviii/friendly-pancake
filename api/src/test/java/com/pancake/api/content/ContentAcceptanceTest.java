package com.pancake.api.content;

import com.pancake.api.content.application.dto.ContentResponse;
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

import static com.pancake.api.content.helper.ContentRequestBuilders.aRequest;
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
        등록된_컨텐츠가_있다("https://www.netflix.com/watch/70106454");
        var 원하는_컨텐츠 = 첫번째_컨텐츠(조회된_컨텐츠_목록이_있다());

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

    private Long 첫번째_컨텐츠(List<ContentResponse> contents) {
        return contents.get(0).getId();
    }

    private ResponseSpec 컨텐츠를_시청한다(long id) {
        return client.get().uri("/api/contents/{id}", id).exchange();
    }

    private void 등록된_컨텐츠가_있다(String url) {
        var request = aRequest().url(url).build();
        client.post().uri("/api/contents")
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Void.class);
    }

    private List<ContentResponse> 조회된_컨텐츠_목록이_있다() {
        return client.get().uri("/api/contents")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(ContentResponse.class)
                .returnResult()
                .getResponseBody();
    }
}
