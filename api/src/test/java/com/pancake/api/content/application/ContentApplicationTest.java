package com.pancake.api.content.application;

import com.pancake.api.content.application.Builders.AddWatchCommandBuilder;
import com.pancake.api.content.application.Builders.SaveContentCommandBuilder;
import com.pancake.api.content.domain.Content;
import com.pancake.api.content.domain.ContentRepository;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Condition;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.pancake.api.content.application.Builders.aContentToSave;
import static com.pancake.api.content.application.Builders.aWatchToAdd;
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

    @DisplayName("컨텐츠를 아이디로 조회한다")
    @Test
    void getById() {
        //given
        var expected = savedContent();

        //when
        var actual = contentService.getContent(expected.getId());

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("시청 가능한 모든 컨텐츠를 조회한다")
    @Test
    void getAll() {
        //given
        savedContent(aContentToSave().title("스파이더맨"));
        savedContent(aContentToSave().title("아이언맨"));
        watchableContent(aContentToSave().title("토르"), aWatchToAdd());

        //when
        var actual = contentService.getAllContents();

        //then
        assertThat(actual).singleElement()
                .is(title("토르"))
                .is(watchable());
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
        var command = aWatchToAdd()
                .url("https://www.netflix.com/watch/0")
                .build();

        //when
        contentService.addWatch(contentId, command);

        //then
        assertThatActualBy(contentId).has(url("https://www.netflix.com/watch/0"));
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

    private void watchableContent(SaveContentCommandBuilder contentToSave, AddWatchCommandBuilder watchToAdd) {
        var content = savedContent(contentToSave);
        contentService.addWatch(content.getId(), watchToAdd.build());
    }

    private Content savedContent() {
        return savedContent(aContentToSave());
    }

    private Content savedContent(SaveContentCommandBuilder contentToSave) {
        return contentService.save(contentToSave.build());
    }

    private AbstractObjectAssert<?, Content> assertThatActualBy(long contentId) {
        var actual = contentRepository.findById(contentId);

        return assertThat(actual).get();
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

    private Condition<Content> url(String expected) {
        return new Condition<>(e -> e.getPlaybackUrl().asString().equals(expected), "url equals %s", expected);
    }

    private Condition<Content> watchable() {
        return new Condition<>(Content::canWatch, "must watchable");
    }

    private Condition<Content> watched() {
        return new Condition<>(Content::isWatched, "isWatched is true");
    }
}