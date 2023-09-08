package com.pancake.api.content.api;

import com.pancake.api.content.application.ContentService;
import com.pancake.api.content.application.dto.ContentRequest;
import com.pancake.api.content.application.dto.ContentResponse;
import com.pancake.api.content.helper.ContentRequests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

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
        var request = ContentRequests.DUMMY;
        var res = responseOf(ContentRequests.DUMMY); // TODO
        given(contentService.save(request)).willReturn(res);

        //when
        var response = client.post().uri("/api/contents")
                .contentType(APPLICATION_JSON)
                .bodyValue(ContentRequests.DUMMY)
                .exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isCreated(),
                spec -> spec.expectBody(ContentResponse.class).isEqualTo(res)
        );
    }

    @Test
    void getAllContentsApi() {
        //given
        var res = List.of(responseOf(ContentRequests.THOR), responseOf(ContentRequests.IRON_MAN));
        given(contentService.getAllContents()).willReturn(res);

        //when
        var response = client.get().uri("/api/contents").exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isOk(),
                spec -> spec.expectBodyList(ContentResponse.class).isEqualTo(res)
        );
    }

    @Test
    void getContentApi() {
        //given
        given(contentService.getContent(1234)).willReturn(responseOf(ContentRequests.THOR));

        //when
        var response = client.get().uri("/api/contents/{id}", 1234).exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isSeeOther(),
                spec -> spec.expectHeader().location(ContentRequests.THOR.getUrl()),
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

    private ContentResponse responseOf(ContentRequest request) {
        return new ContentResponse(
                999L, request.getTitle(), request.getDescription(), request.getUrl(), request.getImageUrl(),
                false
        );
    }
}
