package com.pancake.api.content.application;

import com.pancake.api.content.application.dto.ContentRequest;
import com.pancake.api.content.domain.Content;
import com.pancake.api.content.infra.MemoryContentRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

class ContentServiceTest {

    private final ContentService contentService = new ContentService(new MemoryContentRepository());

    @Test
    void save() {
        //given
        var request = new ContentRequest("https://www.netflix.com/watch/60023642?trackId=14234261", "센과 치히로의 행방불명");

        //when
        var actual = contentService.save(request);

        //then
        assertAll(
                () -> assertThat(actual.id()).isPositive(),
                () -> assertThat(actual.url()).isEqualTo(request.getUrl()),
                () -> assertThat(actual.title()).isEqualTo(request.getTitle())
        );
    }

    @Test
    void getAll() {
        //given
        savedContent("https://www.netflix.com/watch/60023642?trackId=14234261", "센과 치히로의 행방불명");
        savedContent("https://www.netflix.com/watch/60032294?trackId=254245392", "이웃집 토토로");

        //when
        var contents = contentService.getAll();

        //then
        assertThat(contents).extracting(Content::url, Content::title)
                .containsExactly(
                        tuple("https://www.netflix.com/watch/60023642?trackId=14234261", "센과 치히로의 행방불명"),
                        tuple("https://www.netflix.com/watch/60032294?trackId=254245392", "이웃집 토토로")
                );
    }

    @Test
    void watched() {
        //given
        var contentId = savedContent("https://www.netflix.com/watch/60023642?trackId=14234261", "센과 치히로의 행방불명").id();

        //when
        var watched = contentService.watch(contentId);

        //then
        assertThat(watched).isTrue();
    }

    private Content savedContent(String url, String title) {
        return contentService.save(new ContentRequest(url, title));
    }

}