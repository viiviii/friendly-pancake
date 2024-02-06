package com.pancake.api.content.api;

import com.pancake.api.content.application.ContentService;
import com.pancake.api.content.domain.Content;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.pancake.api.content.application.Builders.aContentToSave;
import static com.pancake.api.content.application.Builders.aWatchToAdd;
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
        var request = aContentToSave().build();
        var content = request.toEntity();

        given(contentService.save(request)).willReturn(content);

        //when
        var response = client.post().uri("/api/contents")
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isCreated(),
                spec -> spec.expectBody(ContentResponse.class).isEqualTo(ContentResponse.fromEntity(content))
        );
    }

    @Test
    void getAllContentsApi() {
        //given
        var content1 = content();
        var content2 = content();

        given(contentService.getAllContents()).willReturn(List.of(content1, content2));

        //when
        var response = client.get().uri("/api/contents").exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isOk(),
                spec -> spec.expectBodyList(WatchableContentResponse.class).isEqualTo(
                        List.of(toResponse(content1), toResponse(content2))
                )
        );
    }

    // TODO: 이상하죠
    @Test
    void getContentApi() {
        //given
        var content = content();

        given(contentService.getContent(1234)).willReturn(content);

        //when
        var response = client.get().uri("/api/contents/{id}", 1234).exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isSeeOther(),
                spec -> spec.expectHeader().location(content.getUrl()),
                spec -> spec.expectBody(Void.class)
        );
    }

    @Test
    void addWatch() {
        //given
        var request = aWatchToAdd().build();

        //when
        var response = client.post().uri("/api/contents/{id}/watch", 1L)
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isNoContent(),
                spec -> spec.expectBody(Void.class)
        );
    }

    @Test
    void patchWatchedContentApi() {
        //when
        var response = client.patch().uri("/api/contents/{id}/watched", 1234).exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isNoContent(),
                spec -> spec.expectBody(Void.class)
        );
    }

    private Content content() {
        return new Content("title", "description", "imageUrl");
    }

    private WatchableContentResponse toResponse(Content content) {
        return WatchableContentResponse.fromEntity(content);
    }
}
