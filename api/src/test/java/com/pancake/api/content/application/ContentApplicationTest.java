package com.pancake.api.content.application;

import com.pancake.api.content.application.SaveContentCommandBuilders.SaveContentCommandBuilder;
import com.pancake.api.content.domain.Content;
import com.pancake.api.content.infra.ContentRepository;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Condition;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.pancake.api.content.application.SaveContentCommandBuilders.aSaveContentCommand;
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
        var expected = save(aSaveContentCommand());

        //when
        var actual = contentService.getContent(expected.getId());

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("모든 컨텐츠를 조회한다")
    @Test
    void getAll() {
        //given
        save(aSaveContentCommand().title("토르"));
        save(aSaveContentCommand().title("아이언맨"));

        //when
        var actual = contentService.getAllContents();

        //then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("컨텐츠를 저장한다")
    @Test
    void save() {
        //given
        var command = aSaveContentCommand()
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
        var contentId = save(aSaveContentCommand()).getId();
        var command = new AddWatchCommand("https://www.netflix.com/watch/0");

        //when
        contentService.addWatch(contentId, command);

        //then
        assertThatActualBy(contentId).has(url("https://www.netflix.com/watch/0"));
    }

    @DisplayName("컨텐츠를 시청 처리한다")
    @Test
    void watch() {
        //given
        var contentId = save(aSaveContentCommand()).getId();

        //when
        contentService.watch(contentId);

        //then
        assertThatActualBy(contentId).is(watched());
    }

    private Content save(SaveContentCommandBuilder builder) {
        return contentService.save(builder.build());
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
        return new Condition<>(e -> e.getUrl().equals(expected), "url equals %s", expected);
    }

    private Condition<Content> watched() {
        return new Condition<>(Content::isWatched, "isWatched is true");
    }
}