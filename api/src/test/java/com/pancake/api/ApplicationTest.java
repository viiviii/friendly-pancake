package com.pancake.api;

import com.pancake.api.bookmark.application.BookmarkCustom;
import com.pancake.api.bookmark.application.BookmarkService;
import com.pancake.api.bookmark.domain.Bookmark;
import com.pancake.api.bookmark.domain.BookmarkContent;
import com.pancake.api.content.Builders;
import com.pancake.api.content.ContentProvider;
import com.pancake.api.content.ContentType;
import com.pancake.api.content.application.AddPlayback;
import com.pancake.api.content.application.ContentSaveCommand;
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

import static com.pancake.api.bookmark.Builders.aBookmark;
import static com.pancake.api.bookmark.Builders.aBookmarkCustom;
import static com.pancake.api.content.Builders.aContentSaveCommand;
import static com.pancake.api.content.Builders.aStreaming;
import static com.pancake.api.content.ContentType.movie;
import static com.pancake.api.content.domain.Platform.NETFLIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = NONE, classes = TestConfig.class)
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

        @Autowired
        BookmarkCustom bookmarkCustom;

        @Autowired
        MemoryMovies movies;

        @Test
        void 영화를_북마크한다() {
            //given
            movies.존재한다(MemoryMovies.Item.aItem().id("1").title("토토로").build());

            //when
            var actual = bookmarkCommand(aBookmark()
                    .contentType(movie)
                    .contentId("1")
                    .title("토토로")
                    .build());

            //then
            assertThat(actual).returns("토토로", Bookmark::getRecordTitle)
                    .extracting(Bookmark::getContent)
                    .returns("1", BookmarkContent::id)
                    .returns(movie, BookmarkContent::type);
        }

        @Test
        void 직접_입력한_컨텐츠를_북마크한다() {
            //given
            var command = BookmarkCustom.Command.builder()
                    .title("고독한 토토로")
                    .description("설명")
//                    .url("https://www.netflix.com/watch/0")
                    .imageUrl("http://some.image")
                    .build();

            //when
            var actual = bookmarkCustom.command(command);

            //then
            assertAll(
                    () -> assertThat(actual.getRecordTitle()).isEqualTo("고독한 토토로"),
                    () -> assertThat(actualBy(actual.getContent().id(), Content.class))
                            .returns("고독한 토토로", Content::getTitle)
                            .returns("설명", Content::getDescription)
                            .returns("http://some.image", Content::getImageUrl)
            );
        }

        @Test
        void 목록을_조회한다() {
            //given
            var movie = MemoryMovies.Item.aItem().build();
            movies.존재한다(movie);

            bookmarkCommand(aBookmark()
                    .contentType(ContentType.movie)
                    .contentId(movie.id())
                    .title(movie.title())
                    .build());
            bookmarkCustom.command(aBookmarkCustom().build());

            //when
            var actual = bookmarkService.getList();

            //then
            assertThat(actual).hasSize(2);
        }

        private Bookmark bookmarkCommand(Bookmark bookmark) {
            return bookmarkService.save(bookmark);
        }
    }

    @Nested
    class 컨텐츠 {

        @Test
        void 컨텐츠의_메타데이터를_저장한다() {
            //given
            var command = aContentSaveCommand()
                    .title("토토로")
                    .description("설명")
                    .imageUrl("http://some.image")
                    .build();

            //when
            var content = contentService.save(command);

            //then
            assertThat(actualBy(content.getId(), Content.class))
                    .returns("토토로", Content::getTitle)
                    .returns("설명", Content::getDescription)
                    .returns("http://some.image", Content::getImageUrl);
        }

        @Test
        void 컨텐츠에_시청주소를_추가한다() {
            //given
            var contentId = save(aContentSaveCommand());

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
            var contentId = save(aContentSaveCommand());

            //when
            contentService.watch(contentId);

            //then
            assertThat(actualBy(contentId, Content.class))
                    .returns(true, Content::isWatched);
        }

        @Test
        void 컨텐츠의_이미지를_변경한다() {
            //given
            var contentId = save(aContentSaveCommand().imageUrl("원래 이미지 주소"));

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
            var contentId = save(aContentSaveCommand());
            add(contentId, aStreaming().url("https://www.netflix.com/watch/100000001"));

            //when
            var actual = getWatchUrl.query(contentId, NETFLIX);

            //then
            assertThat(actual).isEqualTo("https://www.netflix.com/watch/100000001");
        }

        @Test
        void 시청할_컨텐츠_목록을_조회한다() {
            //given
            save(aContentSaveCommand().title("스파이더맨"));
            add(save(aContentSaveCommand().title("토르")), aStreaming());

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

    @Test
    void 컨텐츠_타입별_제공자가_모두_존재한다(@Autowired List<ContentProvider> providers) {
        assertThat(providers)
                .extracting(ContentProvider::provideType)
                .containsOnlyOnce(ContentType.values());
    }

    private Long save(ContentSaveCommand.ContentSaveCommandBuilder builder) {
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