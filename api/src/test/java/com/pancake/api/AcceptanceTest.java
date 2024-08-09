package com.pancake.api;

import com.pancake.api.bookmark.BookmarkResponse;
import com.pancake.api.bookmark.BookmarkSaveCommand.BookmarkSaveCommandBuilder;
import com.pancake.api.content.api.ContentResponse;
import com.pancake.api.content.application.ContentSaveCommand.ContentSaveCommandBuilder;
import com.pancake.api.content.domain.Playback;
import com.pancake.api.search.SearchMovie;
import com.pancake.api.setting.api.SettingApiController.PlatformSettingResponse;
import com.pancake.api.watch.application.Catalog;
import com.pancake.api.watch.domain.WatchContent;
import com.pancake.api.watch.domain.WatchOption;
import org.assertj.core.api.Condition;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.pancake.api.bookmark.Builders.aBookmarkSaveCommand;
import static com.pancake.api.content.Builders.*;
import static com.pancake.api.setting.Builders.aEnableSetting;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TestConfig.class)
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
        컨텐츠를_등록하고(시청주소를_추가한다("https://www.netflix.com/watch/70106454"));

        //목표
        var 시청_목록 = 시청_목록을_조회한다();

        //결과
        컨텐츠를_원하는_플랫폼에서_시청한다("넷플릭스")
                .accept(첫번째를_선택(시청_목록.getContents()));
    }

    @Test
    void 사용자는_시청할_플랫폼을_설정할_수_있다() {
        //준비
        컨텐츠를_등록하고(시청주소를_추가한다("https://www.disneyplus.com/video/6e386dd6"));
        컨텐츠를_등록하고(시청주소를_추가한다("https://www.netflix.com/watch/70106454"));
        컨텐츠를_등록하고(시청주소를_추가한다("https://www.netflix.com/watch/10295049"));

        //목표
        var 설정_목록 = 플랫폼_설정_목록을_조회한다();
        플랫폼_활성화_여부를_설정한다("넷플릭스", null)
                .andThen(플랫폼_활성화_여부를_설정한다("디즈니플러스", "1999-12-31T00:00:00Z"))
                .accept(설정_목록);

        //결과
        var 시청_목록 = 시청_목록을_조회한다();
        then(시청_목록.getContents()).are(시청_가능하다("넷플릭스"));
    }

    @Test
    void 사용자는_컨텐츠를_검색하고_북마크_할_수_있다(@Autowired MemoryMetadataRepository 메타데이터) {
        //given
        메타데이터.존재한다(aMetadata().title("귀여븐 포뇨"));

        //when
        var 검색_결과 = 컨텐츠를_검색한다("귀여븐 포뇨");
        첫번째_결과를_북마크로_등록한다().accept(검색_결과);

        //then
        then(북마크_목록을_조회한다()).singleElement()
                .is(제목은("귀여븐 포뇨"));
    }

    @Test
    void 사용자는_직접_입력한_컨텐츠를_북마크_할_수_있다() {
        //given
        var 컨텐츠 = aContentSaveCommand().title("고독한 포뇨");

        //when
        직접_입력한_컨텐츠를_북마크한다().accept(컨텐츠);

        //then
        then(북마크_목록을_조회한다()).singleElement()
                .is(제목은("고독한 포뇨"));
    }

    @Test
    void 나는_컨텐츠를_관리할_수_있다() {
        //준비
        컨텐츠를_등록한다().apply(aContentSaveCommand());

        //목표
        var 컨텐츠_목록 = 컨텐츠_목록을_조회한다();

        컨텐츠의_재생정보를_조회한다().accept(첫번째를_선택(컨텐츠_목록));
        시청주소를_추가한다("https://www.netflix.com/watch/3X0g0a").accept(첫번째를_선택(컨텐츠_목록));
        이미지를_변경한다("https://some.change.image").accept(첫번째를_선택(컨텐츠_목록));
    }

    private List<PlatformSettingResponse> 플랫폼_설정_목록을_조회한다() {
        return client.get().uri("/api/settings/platforms")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(PlatformSettingResponse.class)
                .returnResult().getResponseBody();
    }

    private List<ContentResponse> 컨텐츠_목록을_조회한다() {
        return client.get().uri("/api/contents")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(ContentResponse.class)
                .returnResult().getResponseBody();
    }

    private List<BookmarkResponse> 북마크_목록을_조회한다() {
        return client.get().uri("/api/bookmarks")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(BookmarkResponse.class)
                .returnResult().getResponseBody();
    }

    private Consumer<SearchMovie.Result> 첫번째_결과를_북마크로_등록한다() {
        return result -> {
            var content = 첫번째를_선택(result.contents());
            북마크를_등록한다().apply(aBookmarkSaveCommand()
                    .contentId(content.id())
                    .contentType(content.mediaType())
                    .title(content.title())
            );
        };
    }

    private Consumer<ContentSaveCommandBuilder> 직접_입력한_컨텐츠를_북마크한다() {
        return request -> client.post().uri("/api/bookmarks/customs")
                .contentType(APPLICATION_JSON)
                .bodyValue(request.build())
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    private Function<BookmarkSaveCommandBuilder, BookmarkResponse> 북마크를_등록한다() {
        return builder -> client.post().uri("/api/bookmarks")
                .contentType(APPLICATION_JSON)
                .bodyValue(builder.build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(BookmarkResponse.class)
                .returnResult().getResponseBody();
    }

    private Function<ContentSaveCommandBuilder, ContentResponse> 컨텐츠를_등록한다() {
        return builder -> client.post().uri("/api/contents")
                .contentType(APPLICATION_JSON)
                .bodyValue(builder.build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(ContentResponse.class)
                .returnResult().getResponseBody();
    }

    private SearchMovie.Result 컨텐츠를_검색한다(String title) {
        return client.get().uri("/api/search/contents?query={title}", title)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(SearchMovie.Result.class)
                .returnResult().getResponseBody();

    }

    private Catalog 시청_목록을_조회한다() {
        return client.get().uri("/api/watches")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Catalog.class)
                .returnResult().getResponseBody();
    }

    private Consumer<ContentResponse> 컨텐츠의_재생정보를_조회한다() {
        return content -> client.get().uri("/api/contents/{id}/playbacks", content.getId())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Playback.class)
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

    private Consumer<WatchContent> 컨텐츠를_원하는_플랫폼에서_시청한다(String platformLabel) {
        return content -> {
            var option = content.getOptions().stream()
                    .filter(hasPlatformWith(platformLabel))
                    .findAny().orElseThrow();
            client.get().uri("/api/watch/{id}/{platformName}", content.getId(), option.getPlatform().name())
                    .exchange()
                    .expectStatus().isSeeOther()
                    .expectHeader().location(option.getUrl());
        };
    }

    private Consumer<List<PlatformSettingResponse>> 플랫폼_활성화_여부를_설정한다(String platformLabel, String disableFrom) {
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

    private void 컨텐츠를_등록하고(Consumer<ContentResponse> fn) {
        var response = 컨텐츠를_등록한다().apply(aContentSaveCommand());

        fn.accept(response);
    }

    private <T> T 첫번째를_선택(List<T> list) {
        return list.stream().findFirst().orElseThrow();
    }

    private Condition<WatchContent> 시청_가능하다(String platformLabel) {
        return new Condition<>(e -> e.getOptions().stream().anyMatch(hasPlatformWith(platformLabel)),
                "%s에서 시청 가능하다", platformLabel);
    }

    private Condition<BookmarkResponse> 제목은(String title) {
        return new Condition<>(e -> e.recordTitle().equals(title),
                "컨텐츠 제목은 %s이다", title);
    }

    private Predicate<WatchOption> hasPlatformWith(String label) {
        return option -> option.getPlatform().label().equals(label);
    }

}
