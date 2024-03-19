package com.pancake.api;

import com.pancake.api.content.Builders;
import com.pancake.api.content.application.AddPlayback;
import com.pancake.api.content.application.ContentService;
import com.pancake.api.content.domain.Content;
import com.pancake.api.content.domain.Playback;
import com.pancake.api.watch.application.GetContentsToWatch;
import com.pancake.api.watch.application.GetContentsToWatchResult;
import com.pancake.api.watch.application.GetWatchUrl;
import jakarta.persistence.EntityManager;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.pancake.api.content.Builders.aMetadata;
import static com.pancake.api.content.Builders.aStreaming;
import static com.pancake.api.content.domain.Platform.DISNEY_PLUS;
import static com.pancake.api.content.domain.Platform.NETFLIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = NONE)
@TestPropertySource(properties = "spring.flyway.clean-disabled=false")
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

    @Test
    void 컨텐츠를_저장한다() {
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
        assertThat(actualBy(contentId, Content.class).getPlaybacks())
                .extracting(Playback::getPlatform, Playback::getUrl)
                .containsExactly(
                        tuple(NETFLIX, "https://www.netflix.com/watch/0"),
                        tuple(DISNEY_PLUS, "https://www.disneyplus.com/video/1")
                );
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
    void 시청주소를_조회한다(@Autowired GetWatchUrl getWatchUrl) {
        //given
        var contentId = save(aMetadata());
        add(contentId, aStreaming().url("https://www.netflix.com/watch/100000001"));

        //when
        var actual = getWatchUrl.query(contentId, NETFLIX);

        //then
        assertThat(actual).isEqualTo("https://www.netflix.com/watch/100000001");
    }

    @Test
    void 시청할_컨텐츠_목록을_조회한다(@Autowired GetContentsToWatch getContentsToWatch) {
        //given
        save(aMetadata().title("스파이더맨"));
        add(save(aMetadata().title("토르")), aStreaming());

        //when
        var actual = getContentsToWatch.query();

        //then
        assertThat(actual).singleElement()
                .returns("토르", GetContentsToWatchResult::getTitle);
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
}