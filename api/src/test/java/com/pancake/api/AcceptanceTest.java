package com.pancake.api;

import com.pancake.api.content.api.ContentResponse;
import com.pancake.api.content.domain.Playback;
import com.pancake.api.content.infra.api.MockTmdbServerConfiguration;
import com.pancake.api.content.infra.api.MockTmdbServerConfiguration.MockTmdbServer;
import com.pancake.api.search.SearchContentMetadata;
import com.pancake.api.setting.api.SettingApiController.PlatformSettingResponse;
import com.pancake.api.watch.application.Catalog;
import com.pancake.api.watch.domain.WatchOption;
import org.assertj.core.api.Condition;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec.ResponseSpecConsumer;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.pancake.api.content.Builders.*;
import static com.pancake.api.setting.Builders.aEnableSetting;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = {"spring.flyway.clean-disabled=false", "api.tmdb.token=test-token"})
@Import(MockTmdbServerConfiguration.class)
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
        등록된_컨텐츠에(시청주소를_추가한다("https://www.netflix.com/watch/70106454"));

        //목표
        var 결과 = 시청할_컨텐츠_목록의(this::첫번째_컨텐츠를_넷플릭스에서_시청한다);

        //결과
        결과.expectAll(
                컨텐츠를_시청할_수_있는_주소로_이동된다("https://www.netflix.com/watch/70106454")
        );
    }

    @Test
    void 사용자는_원하는_플랫폼의_컨텐츠만_조회할_수_있다() {
        //given
        등록된_컨텐츠에(시청주소를_추가한다("https://www.disneyplus.com/video/6e386dd6"));
        등록된_컨텐츠에(시청주소를_추가한다("https://www.netflix.com/watch/70106454"));
        등록된_컨텐츠에(시청주소를_추가한다("https://www.netflix.com/watch/10295049"));

        //when
        플랫폼_설정의(활성화_여부를_설정한다("넷플릭스", null)
                .andThen(활성화_여부를_설정한다("디즈니플러스", "1999-12-31T00:00:00Z")));

        //then
        then(시청할_컨텐츠_목록의(this::모든_시청_옵션)).are(넷플릭스에서_시청_가능하다());
    }

    @Test
    void 사용자는_컨텐츠를_검색하고_등록할_수_있다(@Autowired MockTmdbServer mock) {
        //given
        mock.request("/search/movie?query={title}&language=ko", "포뇨")
                .willReturn(aTmdbPage()
                        .result(aTmdbMovie().title("포뇨").build())
                        .build());

        //when
        var response = 컨텐츠를_검색한다("포뇨");
//        컨텐츠를_등록한다("포뇨", "외부_API의_컨텐츠_아이디");

        //then
//        then(모든_컨텐츠_목록의(this::첫번째를_선택))
//                .is(제목은("포뇨"));
    }


    @Test
    void 나는_컨텐츠를_관리할_수_있다() {
        //목표
        등록된_컨텐츠에(시청주소를_추가한다("https://www.netflix.com/watch/3X0g0a")
                .andThen(이미지를_변경한다("https://some.change.image")));

        //결과
        then(모든_컨텐츠_목록의(this::첫번째를_선택))
                .is(이미지는("https://some.change.image"))
                .is(조회된_시청주소는("https://www.netflix.com/watch/3X0g0a"));
    }

    private ResponseSpecConsumer 컨텐츠를_시청할_수_있는_주소로_이동된다(String url) {
        return spec -> spec.expectHeader().location(url);
    }

    private Condition<WatchOption> 넷플릭스에서_시청_가능하다() {
        return new Condition<>(e -> e.getPlatform().name().equals("NETFLIX"), "넷플릭스에서_시청_가능하다");
    }

    private Condition<ContentResponse> 이미지는(String imageUrl) {
        return new Condition<>(e -> e.getImageUrl().equals(imageUrl), "이미지가 같다");
    }

    private Condition<ContentResponse> 조회된_시청주소는(String url) {
        return new Condition<>(content -> 컨텐츠의_모든_재생정보를_조회한다(content).stream()
                .allMatch(e -> e.getUrl().equals(url)), "시청주소가 같다");
    }

    private List<WatchOption> 모든_시청_옵션(Catalog catalog) {
        return catalog.getContents().stream().flatMap(c -> c.getOptions().stream()).toList();
    }

    private ContentResponse 첫번째를_선택(List<ContentResponse> contents) {
        return contents.get(0);
    }

    private ResponseSpec 첫번째_컨텐츠를_넷플릭스에서_시청한다(Catalog catalog) {
        var content = catalog.getContents().stream().findFirst().orElseThrow();

        return client.get().uri("/api/watch/{id}/{platformName}", content.getId(), "NETFLIX").exchange();
    }

    private List<Playback> 컨텐츠의_모든_재생정보를_조회한다(ContentResponse content) {
        return client.get().uri("/api/contents/{id}/playbacks", content.getId())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Playback.class)
                .returnResult().getResponseBody();
    }

    private <T> T 시청할_컨텐츠_목록의(Function<Catalog, T> fn) {
        var response = client.get().uri("/api/watches")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Catalog.class)
                .returnResult().getResponseBody();

        return fn.apply(response);
    }

    private <T> T 모든_컨텐츠_목록의(Function<List<ContentResponse>, T> fn) {
        var response = client.get().uri("/api/contents")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(ContentResponse.class)
                .returnResult().getResponseBody();

        return fn.apply(response);
    }

    private void 등록된_컨텐츠에(Consumer<ContentResponse> fn) {
        var response = client.post().uri("/api/contents")
                .contentType(APPLICATION_JSON)
                .bodyValue(aMetadata().build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(ContentResponse.class)
                .returnResult().getResponseBody();

        fn.accept(response);
    }

    private void 플랫폼_설정의(Consumer<List<PlatformSettingResponse>> fn) {
        var response = client.get().uri("/api/settings/platforms")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(PlatformSettingResponse.class)
                .returnResult().getResponseBody();

        fn.accept(response);
    }

    private SearchContentMetadata.Result 컨텐츠를_검색한다(String title) {
        return client.get().uri("/api/search/contents?query={title}", title)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(SearchContentMetadata.Result.class)
                .returnResult().getResponseBody();

    }

    private Consumer<ContentResponse> 시청주소를_추가한다(String url) {
        return content -> client.post().uri("/api/contents/{id}/playbacks", content.getId())
                .contentType(APPLICATION_JSON)
                .bodyValue(aStreaming().url(url).build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Void.class);
    }

    private Consumer<ContentResponse> 이미지를_변경한다(String imageUrl) {
        return content -> client.patch().uri("/api/contents/{id}/image", content.getId())
                .contentType(APPLICATION_JSON)
                .bodyValue(imageUrl)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Void.class);
    }

    private Consumer<List<PlatformSettingResponse>> 활성화_여부를_설정한다(String platformLabel, String disableFrom) {
        return settings -> {
            var platformName = settings.stream()
                    .filter(e -> e.platformLabel().equals(platformLabel))
                    .map(PlatformSettingResponse::platformName)
                    .findAny().orElseThrow();
            client.put().uri("/api/settings/platforms/{name}", platformName)
                    .contentType(APPLICATION_JSON)
                    .bodyValue(aEnableSetting().disableFrom(disableFrom).build())
                    .exchange()
                    .expectStatus().is2xxSuccessful()
                    .expectBody(Void.class);
        };
    }

}
