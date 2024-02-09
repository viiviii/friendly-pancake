package com.pancake.api.content.application;

import com.pancake.api.content.application.Builders.SaveContentCommandBuilder;
import com.pancake.api.content.domain.Content;
import com.pancake.api.content.domain.ContentRepository;
import com.pancake.api.content.domain.Platform;
import org.assertj.core.api.Condition;
import org.assertj.core.api.ObjectAssert;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.pancake.api.content.application.Builders.aContentToSave;
import static com.pancake.api.content.application.Builders.aPlaybackToAdd;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@SpringBootTest(webEnvironment = NONE)
@TestPropertySource(properties = "spring.flyway.clean-disabled=false")
class ContentApplicationTest {

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    ContentService contentService;

    @AfterEach
    void cleanUp(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @DisplayName("시청 가능한 모든 컨텐츠를 조회한다")
    @Test
    void getAll() {
        //given
        savedContent(aContentToSave().title("스파이더맨"));
        savedContent(aContentToSave().title("아이언맨"));
        watchableContent(
                aContentToSave().title("토르"),
                aPlaybackToAdd().url("https://www.netflix.com/watch/100000001"));

        //when
        var actual = contentService.getAllContents();

        //then
        assertThat(actual).singleElement()
                .is(title("토르"))
                .is(watchable())
                .has(platform(Platform.NETFLIX));
    }

    @DisplayName("컨텐츠를 저장한다")
    @Test
    void save() {
        //given
        var command = aContentToSave()
                .title("토토로")
                .description("설명")
                .imageUrl("http://some.image")
                .build();

        //when
        var content = contentService.save(command);

        //then
        assertThatActualBy(content.getId())
                .has(title("토토로"))
                .has(description("설명"))
                .has(imageUrl("http://some.image"));
    }

    @DisplayName("컨텐츠에 시청 주소를 추가한다")
    @Test
    void addWatchToContent() {
        //given
        var contentId = savedContent().getId();

        //when
        contentService.addPlayback(
                contentId,
                aPlaybackToAdd().url("https://www.netflix.com/watch/0").build()
        );
        contentService.addPlayback(
                contentId,
                aPlaybackToAdd().url("https://www.disneyplus.com/video/1").build()
        );

        //then
        assertThatActualBy(contentId)
                .extracting(Content::getPlaybacks).asList()
                .hasSize(2);
    }

    @DisplayName("컨텐츠를 시청 처리한다")
    @Test
    void watch() {
        //given
        var contentId = savedContent().getId();

        //when
        contentService.watch(contentId);

        //then
        assertThatActualBy(contentId).is(watched());
    }

    private void watchableContent(SaveContentCommandBuilder content, Builders.AddPlaybackCommandBuilder playback) {
        var contentId = savedContent(content).getId();
        contentService.addPlayback(contentId, playback.build());
    }

    private Content savedContent() {
        return savedContent(aContentToSave());
    }

    private Content savedContent(SaveContentCommandBuilder contentToSave) {
        return contentService.save(contentToSave.build());
    }

    private ObjectAssert<Content> assertThatActualBy(long contentId) {
        final var actual = contentRepository.findById(contentId).orElseThrow();

        return assertThat(actual);
    }

    private Condition<Content> title(String expected) {
        return new Condition<>(e -> e.getTitle().equals(expected), "title equals %s", expected);
    }

    private Condition<Content> description(String expected) {
        return new Condition<>(e -> e.getDescription().equals(expected), "description equals %s", expected);
    }

    private Condition<Content> imageUrl(String expected) {
        return new Condition<>(e -> e.getImageUrl().equals(expected), "image url equals %s", expected);
    }

    private Condition<Content> watchable() {
        return new Condition<>(Content::canWatch, "must watchable");
    }

    private Condition<Content> watched() {
        return new Condition<>(Content::isWatched, "isWatched is true");
    }

    private Condition<Content> platform(Platform expected) {
        return new Condition<>(e -> e.getPlaybacks().stream().anyMatch(p -> p.getPlatform().equals(expected)),
                "has expected platform");
    }
}