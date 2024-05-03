package com.pancake.api.content.infra;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(components = TmdbApiClientConfiguration.class, properties = "api.tmdb.token=test-token")
class TmdbApiClientConfigurationTest {

    @Autowired
    RestClient client;

    @Autowired
    MockRestServiceServer server;

    @Test
    void baseUrl() {
        //given
        server.expect(requestTo("https://api.themoviedb.org/3/movie/1"))
                .andRespond(withSuccess());

        //when
        client.get()
                .uri("/movie/1")
                .retrieve()
                .toBodilessEntity();

        //then
        server.verify();
    }

    @Test
    void defaultHeaders() {
        //given
        server.expect(requestTo("https://api.themoviedb.org/3"))
                .andExpect(header(AUTHORIZATION, "Bearer test-token"))
                .andExpect(header(ACCEPT, APPLICATION_JSON_VALUE))
                .andRespond(withSuccess());

        //when
        client.get()
                .retrieve()
                .toBodilessEntity();

        //then
        server.verify();
    }

    @Test
    void deserializeJsonAsSnakeCase() {
        //given
        server.expect(requestTo("https://api.themoviedb.org/3"))
                .andRespond(withSuccess("{\"snake_case_key\":1}", APPLICATION_JSON));

        //when
        var actual = client.get()
                .retrieve()
                .body(Response.class);

        //then
        assertThat(actual).isNotNull().returns(1, Response::snakeCaseKey);
    }

    @Test
    void serializeJsonAsSnakeCase() {
        //given
        server.expect(requestTo("https://api.themoviedb.org/3"))
                .andExpect(content().string("{\"snake_case_key\":3}"))
                .andRespond(withSuccess());

        //when
        client.post()
                .body(new Response(3))
                .retrieve()
                .toBodilessEntity();

        //then
        server.verify();
    }

    private record Response(int snakeCaseKey) {
    }
}