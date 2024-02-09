package com.pancake.api.content;

import com.pancake.api.content.api.ContentResponse;
import com.pancake.api.content.api.WatchableContentResponse;
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

import static com.pancake.api.content.application.Builders.aContentToSave;
import static com.pancake.api.content.application.Builders.aPlaybackToAdd;
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
        컨텐츠에_시청주소를_추가한다(원하는_컨텐츠, "https://www.disneyplus.com/ko-kr/video/6e386dd6");
        var 시청_아이디 = 시청할_컨텐츠의_플랫폼을_선택한다(조회된_컨텐츠_목록이_있다(), 원하는_컨텐츠, "넷플릭스");

        //목표
        var 결과 = 컨텐츠를_시청한다(시청_아이디);

        //결과
        결과.expectAll(
                컨텐츠를_시청할_수_있는_주소로_이동된다("https://www.netflix.com/watch/70106454")
        );
    }

    private long 시청할_컨텐츠의_플랫폼을_선택한다(List<WatchableContentResponse> contents, long contentId, String platformName) {
        var playback = contents.stream()
                .filter(e -> e.getId().equals(contentId))
                .flatMap(e -> e.getPlaybacks().stream())
                .filter(e -> e.getPlatformName().equals(platformName))
                .findAny()
                .orElseThrow();

        return playback.getId();
    }

    private ResponseSpecConsumer 컨텐츠를_시청할_수_있는_주소로_이동된다(String url) {
        return spec -> spec.expectHeader().location(url);
    }

    private ResponseSpec 컨텐츠를_시청한다(long id) {
        return client.get().uri("/api/contents/{id}", id).exchange();
    }

    private ContentResponse 등록된_컨텐츠가_있다() {
        var request = aContentToSave().build();

        return client.post().uri("/api/contents")
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(ContentResponse.class)
                .returnResult().getResponseBody();
    }

    private void 컨텐츠에_시청주소를_추가한다(long contentId, String url) {
        var request = aPlaybackToAdd().url(url).build();
        client.post().uri("/api/contents/{id}/playbacks", contentId)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Void.class);
    }

    private List<WatchableContentResponse> 조회된_컨텐츠_목록이_있다() {
        return client.get().uri("/api/contents")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(WatchableContentResponse.class)
                .returnResult()
                .getResponseBody();
    }
}
