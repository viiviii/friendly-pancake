package com.pancake.api.bookmark;

import com.pancake.api.bookmark.api.BookmarkApiController;
import com.pancake.api.bookmark.api.BookmarkResponse;
import com.pancake.api.bookmark.application.BookmarkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.pancake.api.bookmark.Builders.aBookmarkSaveCommand;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest(BookmarkApiController.class)
class BookmarkApiControllerTest {

    @Autowired
    WebTestClient client;

    @MockBean
    BookmarkService bookmarkService;

    @Test
    void post() {
        //given
        var request = aBookmarkSaveCommand().build();
        var bookmark = request.toBookmark();

        given(bookmarkService.save(request)).willReturn(bookmark);

        //when
        var response = client.post().uri("/api/bookmarks")
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isCreated(),
                spec -> spec.expectBody(BookmarkResponse.class).isEqualTo(new BookmarkResponse(bookmark))
        );
    }

    @Test
    void getList() {
        //given
        var bookmark = aBookmarkSaveCommand().build().toBookmark();

        given(bookmarkService.getList()).willReturn(List.of(bookmark));

        //when
        var response = client.get().uri("/api/bookmarks").exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isOk(),
                spec -> spec.expectBodyList(BookmarkResponse.class)
                        .isEqualTo(List.of(new BookmarkResponse(bookmark)))
        );
    }
}
