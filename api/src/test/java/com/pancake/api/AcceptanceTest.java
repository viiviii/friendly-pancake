package com.pancake.api;

import com.pancake.api.content.api.ContentResponse;
import com.pancake.api.content.domain.Playback;
import com.pancake.api.watch.application.Catalog;
import com.pancake.api.watch.domain.WatchContent;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec.ResponseSpecConsumer;

import java.time.LocalDate;
import java.util.List;
import java.util.function.BiFunction;

import static com.pancake.api.content.Builders.aMetadata;
import static com.pancake.api.content.Builders.aStreaming;
import static com.pancake.api.setting.Builders.aEnabledSetting;
import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
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
    void 사용자는_컨텐츠를_시청할_수_있다() {
        //준비
        var 컨텐츠_아이디 = 컨텐츠를_등록한다().getId();
        컨텐츠에_시청주소를_추가한다(컨텐츠_아이디, "https://www.disneyplus.com/video/6e386dd6");
        컨텐츠에_시청주소를_추가한다(컨텐츠_아이디, "https://www.netflix.com/watch/70106454");

        //목표
        var 결과 = 시청할_컨텐츠의("NETFLIX", this::컨텐츠를_시청한다);

        //결과
        결과.expectAll(
                컨텐츠를_시청할_수_있는_주소로_이동된다("https://www.netflix.com/watch/70106454")
        );
    }

    @Test
    void 사용자는_원하는_플랫폼의_컨텐츠만_조회할_수_있다() {
        //given
        컨텐츠에_시청주소를_추가한다(컨텐츠를_등록한다().getId(), "https://www.disneyplus.com/video/6e386dd6");
        컨텐츠에_시청주소를_추가한다(컨텐츠를_등록한다().getId(), "https://www.netflix.com/watch/70106454");
        컨텐츠에_시청주소를_추가한다(컨텐츠를_등록한다().getId(), "https://www.netflix.com/watch/10295049");

        //when
        시청할_플랫폼을_설정한다("NETFLIX", parse("2099-12-31"));
        시청할_플랫폼을_설정한다("DISNEY_PLUS", parse("1999-12-31"));

        //then
        assertThat(시청할_컨텐츠들을_조회한다().getContents())
                .flatExtracting(WatchContent::getOptions)
                .allSatisfy(e -> assertThat(e.getPlatform().name()).isEqualTo("NETFLIX"));
    }


    @Test
    void 나는_컨텐츠를_관리할_수_있다() {
        //준비
        컨텐츠를_등록한다();
        컨텐츠를_등록한다();
        컨텐츠를_등록한다();
        var 컨텐츠_아이디 = 컨텐츠를_선택(모든_컨텐츠를_조회한다()).getId();

        //목표
        컨텐츠의_이미지를_변경한다(컨텐츠_아이디, "https://some.change.image");
        컨텐츠에_시청주소를_추가한다(컨텐츠_아이디, "https://www.netflix.com/watch/3X0g0a");

        //결과
        assertAll(
                () -> assertThat(컨텐츠를_선택(모든_컨텐츠를_조회한다()))
                        .returns("https://some.change.image", ContentResponse::getImageUrl),
                () -> assertThat(컨텐츠의_모든_재생정보를_조회한다(컨텐츠_아이디)).singleElement()
                        .returns("https://www.netflix.com/watch/3X0g0a", Playback::getUrl)
        );
    }

    private ResponseSpec 시청할_컨텐츠의(String platformName, BiFunction<Long, String, ResponseSpec> 컨텐츠_시청) {
        var 시청할_컨텐츠 = 시청할_컨텐츠들을_조회한다()
                .getContents().stream()
                .findAny().orElseThrow();
        var 시청옵션 = 시청할_컨텐츠.getOptions().stream()
                .filter(e -> e.getPlatform().name().equals(platformName))
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

    private void 시청할_플랫폼을_설정한다(String platformName, LocalDate disableAt) {
        var request = aEnabledSetting().disabledAt(disableAt).build();
        client.put().uri("/api/settings/platforms/{name}", platformName)
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

    private List<ContentResponse> 모든_컨텐츠를_조회한다() {
        return client.get().uri("/api/contents")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(ContentResponse.class)
                .returnResult().getResponseBody();
    }

    private List<Playback> 컨텐츠의_모든_재생정보를_조회한다(Long contentId) {
        return client.get().uri("/api/contents/{id}/playbacks", contentId)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Playback.class)
                .returnResult().getResponseBody();
    }

    private ContentResponse 컨텐츠를_선택(List<ContentResponse> contents) {
        return contents.get(0);
    }

    private void 컨텐츠의_이미지를_변경한다(Long id, String imageUrl) {
        client.patch().uri("/api/contents/{id}/image", id)
                .contentType(APPLICATION_JSON)
                .bodyValue(imageUrl)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Void.class);
    }
}
