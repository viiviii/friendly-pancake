package com.pancake.api.content.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static com.pancake.api.content.Builders.aSearchMovieResult;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class TmdbApiClientTest {

    RestClient.Builder restClientBuilder = RestClient.builder();

    MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();

    TmdbApiClient tmdbApi = new TmdbApiClient(restClientBuilder.build());

    // TODO: 이 분 이상해졌죠
    @Test
    void findAllByTitle() {
        //given
        var expect = TmdbPage.<SearchMovieResult>builder()
                .page(1)
                .totalResults(1)
                .result(aSearchMovieResult().build())
                .build();
        server.expect(requestTo("/search/movie?query=IronMan&language=ko"))
                .andExpect(method(GET))
                .andRespond(withSuccess(asJson(expect), APPLICATION_JSON));

        //when
        var actual = tmdbApi.findAllByTitle("IronMan");

        //then
        assertThat(actual).isEqualTo(expect.toPage().map(SearchMovieResult::toMetadata));
    }

    private String asJson(Object object) {
        try {
            var objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}