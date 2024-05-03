package com.pancake.api.content.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.MockServerRestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseActions;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE;
import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@TestConfiguration
public class MockTmdbServerConfiguration {

    @Bean
    MockServerRestClientCustomizer mockTmdbServerCustomizer() {
        return new MockServerRestClientCustomizer();
    }

    @Bean
    TmdbMockServer mockTmdbServer(MockServerRestClientCustomizer mockTmdbServerCustomizer, ObjectMapper objectMapper) {
        return new TmdbMockServer(mockTmdbServerCustomizer, objectMapper);
    }

    public static final class TmdbMockServer {

        private final MockRestServiceServer server;
        private final ObjectMapper objectMapper;
        private ResponseActions actions;

        public TmdbMockServer(MockServerRestClientCustomizer customizer, ObjectMapper objectMapper) {
            this.server = customizer.getServer();
            this.objectMapper = objectMapper.copy().setPropertyNamingStrategy(SNAKE_CASE);
        }

        public TmdbMockServer request(String path, Object... uriVars) {
            actions = server.expect(requestToUriTemplate(format("https://api.themoviedb.org/3/%s", path), uriVars));
            return this;
        }
        
        public void willReturn(Object body) {
            actions.andRespond(withSuccess()
                    .contentType(APPLICATION_JSON)
                    .body(asJson(body)));
        }

        private String asJson(Object object) {
            try {
                return objectMapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
