package com.pancake.api.content.api;

import com.pancake.api.content.application.ContentService;
import com.pancake.api.content.application.dto.ContentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.pancake.api.content.Fixtures.Netflix.PONYO;
import static com.pancake.api.content.Fixtures.Netflix.TOTORO;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest(ContentApiController.class)
class ContentApiControllerTest {

    @MockBean
    ContentService contentService;

    @Autowired
    WebTestClient client;

    @Test
    void postContentApi() {
        //given
        given(contentService.save(TOTORO.REQUEST)).willReturn(TOTORO.CONTENT);

        //when
        var response = client.post().uri("/api/contents")
                .contentType(APPLICATION_JSON)
                .bodyValue(TOTORO.REQUEST)
                .exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isCreated(),
                spec -> spec.expectBody(ContentResponse.class).isEqualTo(TOTORO.RESPONSE)
        );
    }

    @Test
    void getAllContentsApi() {
        //given
        given(contentService.getAllContents()).willReturn(List.of(TOTORO.CONTENT, PONYO.CONTENT));

        //when
        var response = client.get().uri("/api/contents").exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isOk(),
                spec -> spec.expectBodyList(ContentResponse.class).isEqualTo(List.of(TOTORO.RESPONSE, PONYO.RESPONSE))
        );
    }

    @Test
    void getContentApi() {
        //given
        given(contentService.getContent(1234)).willReturn(TOTORO.CONTENT);

        //when
        var response = client.get().uri("/api/contents/{id}", 1234).exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isSeeOther(),
                spec -> spec.expectHeader().location(TOTORO.CONTENT.url()),
                spec -> spec.expectBody(Void.class)
        );
    }

    @Test
    void patchWatchedContentApi() {
        //given
        given(contentService.watch(1234)).willReturn(true);

        //when
        var response = client.patch().uri("/api/contents/{id}/watched", 1234).exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isOk(),
                spec -> spec.expectBody(Boolean.class).isEqualTo(true)
        );
    }
}
