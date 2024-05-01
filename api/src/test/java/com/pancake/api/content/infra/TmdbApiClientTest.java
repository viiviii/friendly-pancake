package com.pancake.api.content.infra;

import com.pancake.api.content.application.ContentMetadata;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(TmdbApiClient.class)
@Import(TmdbApiClientConfiguration.class)
class TmdbApiClientTest {

    @Autowired
    TmdbApiClient client;

    @Autowired
    MockRestServiceServer server;

    @Test
    void findAllByTitle() {
        //given
        server.expect(requestToUriTemplate("https://api.themoviedb.org/3/search/movie?query={title}&language=ko", "이웃집 토토로"))
                .andExpect(method(GET))
                .andRespond(withSuccess("""
                        {"page":1,"results":[{"adult":false,"backdrop_path":"/fxYazFVeOCHpHwuqGuiqcCTw162.jpg",
                        "genre_ids":[14,16,10751],"id":8392,"original_language":"ja","original_title":"となりのトトロ",
                        "overview":"1955년 일본의 아름다운 시골 마을...",
                        "popularity":167.395,"poster_path":"/some-poster-path.jpg","release_date":"1988-04-16",
                        "title":"이웃집 토토로","video":false,"vote_average":8.073,"vote_count":7599}],
                        "total_pages":1,"total_results":1}""", APPLICATION_JSON));

        //when
        var actual = client.findAllByTitle("이웃집 토토로");

        //then
        assertThat(actual.getContent()).singleElement()
                .returns("이웃집 토토로", ContentMetadata::getTitle)
                .returns("/some-poster-path.jpg", ContentMetadata::getImageUrl)
                .returns("1955년 일본의 아름다운 시골 마을...", ContentMetadata::getDescription);
    }
}