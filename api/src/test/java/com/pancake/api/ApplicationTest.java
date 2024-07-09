package com.pancake.api;

import com.pancake.api.bookmark.Bookmark;
import com.pancake.api.bookmark.BookmarkService;
import com.pancake.api.content.Builders;
import com.pancake.api.content.application.AddPlayback;
import com.pancake.api.content.application.ContentService;
import com.pancake.api.content.domain.Content;
import com.pancake.api.content.domain.Playback;
import com.pancake.api.setting.application.SetEnablePlatform;
import com.pancake.api.setting.domain.Setting;
import com.pancake.api.watch.application.GetContentsToWatch;
import com.pancake.api.watch.application.GetWatchUrl;
import com.pancake.api.watch.domain.WatchContent;
import jakarta.persistence.EntityManager;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;

import static com.pancake.api.bookmark.Builders.aSaveBookmarkCommand;
import static com.pancake.api.content.Builders.aMetadata;
import static com.pancake.api.content.Builders.aStreaming;
import static com.pancake.api.content.domain.Platform.NETFLIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = NONE)
class ApplicationTest {

    @Autowired
    ContentService contentService;

    @Autowired
    AddPlayback addPlayback;

    @Autowired
    EntityManager em;

    @AfterEach
    void cleanUp(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Nested
    class 북마크 {

        @Autowired
        BookmarkService bookmarkService;

        @Test
        void 컨텐츠를_북마크에_추가한다() {
            //given
            var content = aSaveBookmarkCommand()
                    .title("토토로")
                    .contentId("8392")
                    .contentType("movie")
                    .contentSource("TMDB")
                    .build();

            //when
            var actual = bookmarkService.save(content);

            //then
            assertThat(actualBy(actual.getId(), Bookmark.class))
                    .returns("토토로", Bookmark::getRecordTitle)
                    .returns("8392", Bookmark::getContentId)
                    .returns("movie", Bookmark::getContentType)
                    .returns("TMDB", Bookmark::getContentSource);
        }
    }

    @Nested
    class 컨텐츠 {

        @Test
        void 컨텐츠의_메타데이터를_저장한다() {
            //given
            var metadata = aMetadata()
                    .title("토토로")
                    .description("설명")
                    .imageUrl("http://some.image")
                    .build();

            //when
            var content = contentService.save(metadata);

            //then
            assertThat(actualBy(content.getId(), Content.class))
                    .returns("토토로", Content::getTitle)
                    .returns("설명", Content::getDescription)
                    .returns("http://some.image", Content::getImageUrl);
        }

        @Test
        void 컨텐츠에_시청주소를_추가한다() {
            //given
            var contentId = save(aMetadata());

            //when
            addPlayback.command(
                    contentId,
                    aStreaming().url("https://www.netflix.com/watch/0").build()
            );
            addPlayback.command(
                    contentId,
                    aStreaming().url("https://www.disneyplus.com/video/1").build()
            );

            //then
            assertThat(actualAll(Playback.class)).hasSize(2)
                    .extracting(Playback::getUrl).containsExactly(
                            "https://www.netflix.com/watch/0",
                            "https://www.disneyplus.com/video/1");
        }

        @Test
        void 컨텐츠를_시청_처리한다() {
            //given
            var contentId = save(aMetadata());

            //when
            contentService.watch(contentId);

            //then
            assertThat(actualBy(contentId, Content.class))
                    .returns(true, Content::isWatched);
        }

        @Test
        void 컨텐츠의_이미지를_변경한다() {
            //given
            var contentId = save(aMetadata().imageUrl("원래 이미지 주소"));

            //when
            contentService.changeImage(contentId, "바뀐 이미지 주소");

            //then
            assertThat(actualBy(contentId, Content.class))
                    .returns("바뀐 이미지 주소", Content::getImageUrl);
        }
    }

    @Nested
    class 시청 {

        @Autowired
        GetContentsToWatch getContentsToWatch;

        @Autowired
        GetWatchUrl getWatchUrl;

        @Test
        void 시청주소를_조회한다() {
            //given
            var contentId = save(aMetadata());
            add(contentId, aStreaming().url("https://www.netflix.com/watch/100000001"));

            //when
            var actual = getWatchUrl.query(contentId, NETFLIX);

            //then
            assertThat(actual).isEqualTo("https://www.netflix.com/watch/100000001");
        }

        @Test
        void 시청할_컨텐츠_목록을_조회한다() {
            //given
            save(aMetadata().title("스파이더맨"));
            add(save(aMetadata().title("토르")), aStreaming());

            //when
            var actual = getContentsToWatch.query();

            //then
            assertThat(actual.getContents()).singleElement()
                    .returns("토르", WatchContent::getTitle);
        }
    }

    @Nested
    class 설정 {

        @Autowired
        SetEnablePlatform setEnablePlatform;

        @Test
        void 비활성화_날짜로_플랫폼_활성화를_설정한다() {
            //when
            setEnablePlatform.command(NETFLIX, disableFrom("2080-09-01T00:00:00Z"));

            //then
            assertThat(actualBy(NETFLIX, Setting.class))
                    .returns(disableFrom("2080-09-01T00:00:00Z"), Setting::getDisableFrom);
        }

        private Instant disableFrom(String value) {
            return Instant.parse(value);
        }
    }

    private Long save(Builders.ContentMetadataBuilder builder) {
        return contentService.save(builder.build()).getId();
    }

    private void add(long contentId, Builders.ContentStreamingBuilder builder) {
        addPlayback.command(contentId, builder.build());
    }

    private <T> T actualBy(Object id, Class<T> entityClass) {
        return em.find(entityClass, id);
    }

    private <T> List<T> actualAll(Class<T> entityClass) {
        String query = String.format("select e from %s e", entityClass.getSimpleName());
        return em.createQuery(query, entityClass)
                .getResultList();
    }
}