package com.pancake.api.content.api;

import com.pancake.api.content.application.ContentService;
import com.pancake.api.content.application.dto.AddWatchRequest;
import com.pancake.api.content.application.dto.ContentRequest;
import com.pancake.api.content.application.dto.ContentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.pancake.api.content.helper.ContentRequestBuilders.aRequest;
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
        var request = aRequest().build();
        var expectedBody = responseOf(request);
        given(contentService.save(request)).willReturn(expectedBody);

        //when
        var response = client.post().uri("/api/contents")
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isCreated(),
                spec -> spec.expectBody(ContentResponse.class).isEqualTo(expectedBody)
        );
    }

    @Test
    void getAllContentsApi() {
        //given
        var expectedBodyList = List.of(
                responseOf(aRequest().title("토르").build()),
                responseOf(aRequest().title("아이언맨").build())
        );
        given(contentService.getAllContents()).willReturn(expectedBodyList);

        //when
        var response = client.get().uri("/api/contents").exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isOk(),
                spec -> spec.expectBodyList(ContentResponse.class).isEqualTo(expectedBodyList)
        );
    }

    // TODO: 이상하죠
    @Test
    void getContentApi() {
        //given
        given(contentService.getContent(1234))
                .willReturn(new ContentResponse(0L, "", "", "www.netflix.com/watch/1", "", false));

        //when
        var response = client.get().uri("/api/contents/{id}", 1234).exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isSeeOther(),
                spec -> spec.expectHeader().location("www.netflix.com/watch/1"),
                spec -> spec.expectBody(Void.class)
        );
    }

    @Test
    void addWatch() {
        //given
        var request = new AddWatchRequest("www.netflix.com/watch/1");

        //when
        var response = client.post().uri("/api/contents/{id}/watch", 1L)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isCreated(),
                spec -> spec.expectBody(Void.class) // TODO: 응답값 추가
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
        var response = ContentResponse.fromEntity(request.toEntity());
        response.setId(999L);
        return response;
    }
}
