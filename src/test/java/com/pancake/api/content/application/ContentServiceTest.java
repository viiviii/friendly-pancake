package com.pancake.api.content.application;

import com.pancake.api.content.application.dto.ContentRequest;
import com.pancake.api.content.domain.Content;
import com.pancake.api.content.infra.MemoryContentRepository;
import org.junit.jupiter.api.Test;

import static com.pancake.api.content.NetflixConstant.PONYO;
import static com.pancake.api.content.NetflixConstant.TOTORO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

class ContentServiceTest {

    private final ContentService contentService = new ContentService(new MemoryContentRepository());

    @Test
    void save() {
        //given
        var request = new ContentRequest(TOTORO.URL, TOTORO.TITLE);

        //when
        var actual = contentService.save(request);

        //then
        assertAll(
                () -> assertThat(actual.id()).isPositive(),
                () -> assertThat(actual.url()).isEqualTo(TOTORO.URL),
                () -> assertThat(actual.title()).isEqualTo(TOTORO.TITLE)
        );
    }

    @Test
    void getAllContents() {
        //given
        savedContent(TOTORO.URL, TOTORO.TITLE);
        savedContent(PONYO.URL, PONYO.TITLE);

        //when
        var contents = contentService.getAllContents();

        //then
        assertThat(contents).extracting(Content::url, Content::title)
                .containsExactly(
                        tuple(TOTORO.URL, TOTORO.TITLE), // TODO
                        tuple(PONYO.URL, PONYO.TITLE) // TODO
                );
    }

    @Test
    void getUnwatchedContents() {
        //given
        savedContent(TOTORO.URL, TOTORO.TITLE);
        savedContent(PONYO.URL, PONYO.TITLE);

        //when
        var contents = contentService.getUnwatchedContents();

        //then
        assertThat(contents).extracting(Content::url, Content::title)
                .containsExactly(
                        tuple(TOTORO.URL, TOTORO.TITLE),
                        tuple(PONYO.URL, PONYO.TITLE)
                );
    }

    @Test
    void getWatchedContents() {
        //given
        savedContent(TOTORO.URL, TOTORO.TITLE).watch();
        savedContent(PONYO.URL, PONYO.TITLE).watch();

        //when
        var contents = contentService.getWatchedContents();

        //then
        assertThat(contents).extracting(Content::url, Content::title)
                .containsExactly(
                        tuple(TOTORO.URL, TOTORO.TITLE),
                        tuple(PONYO.URL, PONYO.TITLE)
                );
    }

    @Test
    void watch() {
        //given
        var contentId = savedContent(TOTORO.URL, TOTORO.TITLE).id();

        //when
        var watched = contentService.watch(contentId);

        //then
        assertThat(watched).isTrue();
    }

    private Content savedContent(String url, String title) {
        return contentService.save(new ContentRequest(url, title));
    }

}