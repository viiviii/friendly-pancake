package com.pancake.api.content.infra.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(components = TmdbClientConfiguration.class, properties = "api.tmdb.token=test-token")
@SuppressWarnings("NonAsciiCharacters")
class TmdbClientConfigurationTest {

    @Autowired
    TmdbClient client;

    @Autowired
    MockRestServiceServer server;

    @Test
    void 인증은_BearerToken_방식으로_한다() {
        //given
        server.expect(anything())
                .andExpect(header(AUTHORIZATION, "Bearer test-token"))
                .andRespond(withSuccess());

        //when
        clientExchange();

        //then
        server.verify();
    }

    @Test
    void 메타데이터를_한국어로_요청한다() {
        //given
        server.expect(anything())
                .andExpect(queryParam("language", "ko"))
                .andRespond(withSuccess());

        //when
        clientExchange();

        //then
        server.verify();
    }

    @Test
    void JSON_응답_형식은_스네이크_케이스이므로_역직렬화가_가능해야_한다() {
        //given
        server.expect(anything())
                .andExpect(header(ACCEPT, APPLICATION_JSON_VALUE))
                .andRespond(withSuccess("""
                        {"page":1,"results":[],"total_pages":1,"total_results":0}""", APPLICATION_JSON));

        //when
        var actual = clientExchange();

        //then
        assertThat(actual.totalPages()).isEqualTo(1);
    }

    private TmdbPage<TmdbMovie> clientExchange() {
        return client.searchMoviesBy("SomeTitle");
    }
}