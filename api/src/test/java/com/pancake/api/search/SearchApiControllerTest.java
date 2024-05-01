package com.pancake.api.search;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static java.util.Collections.emptyList;
import static org.mockito.BDDMockito.given;


@WebMvcTest(SearchApiController.class)
class SearchApiControllerTest {

    @MockBean
    SearchContentMetadata searchContentMetadata;

    @Autowired
    WebTestClient client;

    @Test
    void searchContentsByTitle() {
        //given
        var expected = aSearchMetadataResult();

        given(searchContentMetadata.queryBy("토토로")).willReturn(expected);

        //when
        var response = client.get().
                uri("/api/search/contents?query={title}", "토토로")
                .exchange();

        //then
        response.expectAll(
                spec -> spec.expectStatus().isOk(),
                spec -> spec.expectBody(SearchContentMetadata.Result.class).isEqualTo(expected)
        );
    }

    private SearchContentMetadata.Result aSearchMetadataResult() {
        return new SearchContentMetadata.Result(false, emptyList());
    }
}