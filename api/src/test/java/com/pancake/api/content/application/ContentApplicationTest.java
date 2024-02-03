package com.pancake.api.content.application;

import com.pancake.api.content.application.dto.AddWatchRequest;
import com.pancake.api.content.application.dto.ContentResponse;
import com.pancake.api.content.domain.Content;
import com.pancake.api.content.helper.ContentRequestBuilders.ContentRequestBuilder;
import com.pancake.api.content.infra.ContentRepository;
import org.assertj.core.api.Condition;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static com.pancake.api.content.helper.ContentRequestBuilders.aRequest;
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

    @DisplayName("컨텐츠에 시청 주소를 추가한다")
    @Test
    void addWatchToContent() {
        //given
        var contentId = save(aRequest()).getId();
        var request = new AddWatchRequest("https://www.netflix.com/watch/0");

        //when
        contentService.addWatch(contentId, request);

        //then
        assertThat(actualBy(contentId)).get()
                .has(url("https://www.netflix.com/watch/0"));
    }

    private ContentResponse save(ContentRequestBuilder request) {
        return contentService.save(request.build());
    }

    private Optional<Content> actualBy(Long id) {
        return contentRepository.findById(id);
    }

    private Condition<Content> url(String expected) {
        return new Condition<>(e -> e.url().equals(expected), "url equals %s", expected);
    }
}