package com.pancake.api.content.infra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SuppressWarnings("NonAsciiCharacters")
class TmdbApiClientTest {

    TmdbApiClient client;

    MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        var builder = RestClient.builder();

        server = MockRestServiceServer.bindTo(builder).build();
        client = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(builder.build()))
                .build()
                .createClient(TmdbApiClient.class);
    }
    
    @Test
    void 제목으로_영화를_검색한다() {
        //given
        server.expect(requestTo("https://api.themoviedb.org/3/search/movie?query=Joker"))
                .andExpect(method(GET))
                .andRespond(withSuccess());

        //when
        client.searchMoviesBy("Joker");

        //then
        server.verify();
    }
}