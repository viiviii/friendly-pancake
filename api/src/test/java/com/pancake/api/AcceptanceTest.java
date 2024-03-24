package com.pancake.api;

import com.pancake.api.content.api.ContentResponse;
import com.pancake.api.content.domain.Content;
import com.pancake.api.content.domain.ContentRepository;
import com.pancake.api.watch.application.Catalog;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec.ResponseSpecConsumer;

import java.util.function.BiFunction;

import static com.pancake.api.content.Builders.aMetadata;
import static com.pancake.api.content.Builders.aStreaming;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = "spring.flyway.clean-disabled=false")
@SuppressWarnings("NonAsciiCharacters")
class AcceptanceTest {

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
        var 컨텐츠_아이디 = 컨텐츠를_등록한다().getId();
        컨텐츠에_시청주소를_추가한다(컨텐츠_아이디, "https://www.disneyplus.com/video/6e386dd6");
        컨텐츠에_시청주소를_추가한다(컨텐츠_아이디, "https://www.netflix.com/watch/70106454");

        //목표
        var 결과 = 시청할_컨텐츠의("넷플릭스", this::컨텐츠를_시청한다);

        //결과
        결과.expectAll(
                컨텐츠를_시청할_수_있는_주소로_이동된다("https://www.netflix.com/watch/70106454")
        );
    }

    @Test
    void 이미지_주소를_변경한다(@Autowired ContentRepository repository) {
        //given
        var 컨텐츠_아이디 = 컨텐츠를_등록한다().getId();

        //when
        client.patch().uri("/api/contents/{id}/image", 컨텐츠_아이디)
                .contentType(APPLICATION_JSON)
                .bodyValue("https://some.other.new.image")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Void.class);
        var actual = repository.findById(컨텐츠_아이디);

        //then
        assertThat(actual).isPresent().get()
                .returns("https://some.other.new.image", Content::getImageUrl);
    }


    private ResponseSpec 시청할_컨텐츠의(String platformLabel, BiFunction<Long, String, ResponseSpec> 컨텐츠_시청) {
        var 시청할_컨텐츠 = 시청할_컨텐츠들을_조회한다()
                .getContents().stream()
                .findAny().orElseThrow();
        var 시청옵션 = 시청할_컨텐츠.getOptions().stream()
                .filter(e -> e.getPlatform().label().equals(platformLabel))
                .findAny().orElseThrow();

        return 컨텐츠_시청.apply(시청할_컨텐츠.getId(), 시청옵션.getPlatform().name());
    }

    private ResponseSpec 컨텐츠를_시청한다(Long id, String platformName) {
        return client.get().uri("/api/watch/{id}/{platformName}", id, platformName).exchange();
    }

    private ResponseSpecConsumer 컨텐츠를_시청할_수_있는_주소로_이동된다(String url) {
        return spec -> spec.expectHeader().location(url);
    }

    private ContentResponse 컨텐츠를_등록한다() {
        var request = aMetadata().build();

        return client.post().uri("/api/contents")
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(ContentResponse.class)
                .returnResult().getResponseBody();
    }

    private void 컨텐츠에_시청주소를_추가한다(long contentId, String url) {
        var request = aStreaming().url(url).build();

        client.post().uri("/api/contents/{id}/playbacks", contentId)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Void.class);
    }

    private Catalog 시청할_컨텐츠들을_조회한다() {
        return client.get().uri("/api/watches")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Catalog.class)
                .returnResult().getResponseBody();
    }
}
