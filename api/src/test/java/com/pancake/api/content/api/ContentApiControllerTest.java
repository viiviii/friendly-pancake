package com.pancake.api.content.api;

import com.pancake.api.content.application.ContentService;
import com.pancake.api.content.application.dto.ContentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

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
        var response = post("/api/contents", TOTORO.REQUEST);

        //then
        response.expectStatus().isCreated()
                .expectBody(ContentResponse.class)
                .isEqualTo(TOTORO.RESPONSE);
    }

    @Test
    void getAllContentsApi() {
        //given
        given(contentService.getAllContents()).willReturn(List.of(TOTORO.CONTENT, PONYO.CONTENT));

        //when
        var response = get("/api/contents");

        //then
        response.expectStatus().isOk()
                .expectBodyList(ContentResponse.class)
                .isEqualTo(List.of(TOTORO.RESPONSE, PONYO.RESPONSE));
    }

    @Test
    void getContentApi() {
        //given
        given(contentService.getContent(1234)).willReturn(TOTORO.CONTENT);

        //when
        var response = get("/api/contents/{id}", 1234);

        //then
        response.expectStatus().isSeeOther()
                .expectHeader().location(TOTORO.CONTENT.url())
                .expectBody(Void.class);
    }

    @Test
    void patchWatchedContentApi() {
        //given
        given(contentService.watch(1234)).willReturn(true);

        //when
        var response = patch("/api/contents/{id}/watched", 1234);

        //then
        response.expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);
    }

    private ResponseSpec get(String path, Object... uriVariables) {
        return client.get().uri(path, uriVariables).exchange();
    }

    private ResponseSpec post(String path, Object body) {
        return client.post().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue(body)
                .exchange();
    }

    private ResponseSpec patch(String path, Object... uriVariables) {
        return client.patch().uri(path, uriVariables).exchange();
    }
}
