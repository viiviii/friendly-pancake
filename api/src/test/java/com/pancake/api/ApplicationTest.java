package com.pancake.api;

import com.pancake.api.content.Builders;
import com.pancake.api.content.application.AddPlayback;
import com.pancake.api.content.application.ContentService;
import com.pancake.api.content.domain.Content;
import com.pancake.api.watch.application.GetContentsToWatch;
import com.pancake.api.watch.application.GetWatchUrl;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Condition;
import org.assertj.core.api.ObjectAssert;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static com.pancake.api.content.Builders.aMetadata;
import static com.pancake.api.content.Builders.aStreaming;
import static com.pancake.api.content.domain.Platform.NETFLIX;
import static org.assertj.core.api.Assertions.assertThat;
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
        assertThatActualBy(Content.class, content.getId())
                .has(title("토토로"))
                .has(description("설명"))
                .has(imageUrl("http://some.image"));
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
        assertThatActualBy(Content.class, contentId)
                .extracting(Content::getPlaybacks).asList()
                .hasSize(2);
    }

    @Test
    void 컨텐츠를_시청_처리한다() {
        //given
        var contentId = save(aMetadata());

        //when
        contentService.watch(contentId);

        //then
        assertThatActualBy(Content.class, contentId).is(watched());
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
        assertThat(actual).singleElement().is(title("토르"));
    }

    private Long save(Builders.ContentMetadataBuilder builder) {
        return contentService.save(builder.build()).getId();
    }

    private void add(long contentId, Builders.ContentStreamingBuilder builder) {
        addPlayback.command(contentId, builder.build());
    }

    private <T> ObjectAssert<T> assertThatActualBy(Class<T> entityClass, Object id) {
        var actual = em.find(entityClass, id);

        return assertThat(actual);
    }

    private <T> Condition<T> title(String expected) {
        return new Condition<>(e -> {
            var actual = ReflectionTestUtils.invokeMethod(e, "getTitle");
            return actual != null && actual.equals(expected);
        }, "title equals %s", expected);
    }

    private Condition<Content> description(String expected) {
        return new Condition<>(e -> e.getDescription().equals(expected), "description equals %s", expected);
    }

    private Condition<Content> imageUrl(String expected) {
        return new Condition<>(e -> e.getImageUrl().equals(expected), "image url equals %s", expected);
    }

    private Condition<Content> watched() {
        return new Condition<>(Content::isWatched, "isWatched is true");
    }
}