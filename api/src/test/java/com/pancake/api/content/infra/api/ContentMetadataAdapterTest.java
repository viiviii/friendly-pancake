package com.pancake.api.content.infra.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.RequestMatcher;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = NONE)
@AutoConfigureMockRestServiceServer
class ContentMetadataAdapterTest {

    @Autowired
    MockRestServiceServer server;

    @Autowired
    ContentMetadataAdapter adapter;

    @Test
    void 제목으로_영화를_검색한다() {
        //given
        var query = "totoro";
        server.expect(requestTo("/search/movie?query={query}&language=ko", query))
                .andExpect(method(GET))
                .andRespond(withSuccess("""
                        {
                          "page": 1,
                          "results": [
                            {
                              "adult": false,
                              "backdrop_path": "/fxYazFVeOCHpHwuqGuiqcCTw162.jpg",
                              "genre_ids": [14, 16, 10751],
                              "id": 8392,
                              "original_language": "ja",
                              "original_title": "となりのトトロ",
                              "overview": "1955년 일본의 아름다운 시골 마을, 상냥하고 의젓한 11살 사츠키와 장난꾸러기에 호기심 많은 4살의 메이 는 사이좋은 자매로 아빠와 함께 도시를 떠나 시골로 이사온다. 자상한 아빠 쿠사카베타츠오는 도쿄에서 대학 연구원이며, 입원 중이지만 따뜻한 미소를 잃지 않는 엄마가 있다. 곧 퇴원하실 엄마를 공기가 맑은 곳에서 맞이하기 위해서다. 숲 한복판에 금방이라도 쓰러질 것처럼 낡은 집을 보며 자매는 새로운 환경에 대한 호기심으로 잔뜩 들뜬다. 그러던 어느 날 사츠키가 학교에 간 동안 메이는 숲에서 정령을 만나다. 메이는 그 정령에게 토토로란 이름을 붙여주는데...",
                              "popularity": 98.858,
                              "poster_path": "/c9zCkL0rTkNQ1HB9cmeFIqbkS50.jpg",
                              "release_date": "1988-04-16",
                              "title": "이웃집 토토로",
                              "video": false,
                              "vote_average": 8.068,
                              "vote_count": 7732
                            }
                          ],
                          "total_pages": 1,
                          "total_results": 1
                        }
                        """, APPLICATION_JSON));

        //when
        var actual = adapter.findAllByTitle(query);

        //then
        assertThat(actual.getContent()).singleElement().hasNoNullFieldsOrProperties();
    }

    @Test
    void 아이디로_영화의_상세정보를_조회한다() {
        //given
        var contentId = "8392";
        server.expect(requestTo("/movie/{movie_id}?language=ko", contentId))
                .andExpect(method(GET))
                .andRespond(withSuccess("""
                        {
                          "adult": false,
                          "backdrop_path": "/fxYazFVeOCHpHwuqGuiqcCTw162.jpg",
                          "belongs_to_collection": null,
                          "budget": 3700000,
                          "genres": [
                            {
                              "id": 14,
                              "name": "판타지"
                            },
                            {
                              "id": 16,
                              "name": "애니메이션"
                            },
                            {
                              "id": 10751,
                              "name": "가족"
                            }
                          ],
                          "homepage": "",
                          "id": 8392,
                          "imdb_id": "tt0096283",
                          "origin_country": [
                            "JP"
                          ],
                          "original_language": "ja",
                          "original_title": "となりのトトロ",
                          "overview": "1955년 일본의 아름다운 시골 마을, 상냥하고 의젓한 11살 사츠키와 장난꾸러기에 호기심 많은 4살의 메이 는 사이좋은 자매로 아빠와 함께 도시를 떠나 시골로 이사온다. 자상한 아빠 쿠사카베타츠오는 도쿄에서 대학 연구원이며, 입원 중이지만 따뜻한 미소를 잃지 않는 엄마가 있다. 곧 퇴원하실 엄마를 공기가 맑은 곳에서 맞이하기 위해서다. 숲 한복판에 금방이라도 쓰러질 것처럼 낡은 집을 보며 자매는 새로운 환경에 대한 호기심으로 잔뜩 들뜬다. 그러던 어느 날 사츠키가 학교에 간 동안 메이는 숲에서 정령을 만나다. 메이는 그 정령에게 토토로란 이름을 붙여주는데...",
                          "popularity": 98.858,
                          "poster_path": "/c9zCkL0rTkNQ1HB9cmeFIqbkS50.jpg",
                          "production_companies": [
                            {
                              "id": 10342,
                              "logo_path": "/eS79pslnoKbWg7t3PMA9ayl0bGs.png",
                              "name": "Studio Ghibli",
                              "origin_country": "JP"
                            },
                            {
                              "id": 12516,
                              "logo_path": null,
                              "name": "Nibariki",
                              "origin_country": "JP"
                            },
                            {
                              "id": 1779,
                              "logo_path": "/41nGzTdCnORYZIZzaGZtfsBwQCl.png",
                              "name": "Tokuma Shoten",
                              "origin_country": "JP"
                            }
                          ],
                          "production_countries": [
                            {
                              "iso_3166_1": "JP",
                              "name": "Japan"
                            }
                          ],
                          "release_date": "1988-04-16",
                          "revenue": 41000000,
                          "runtime": 87,
                          "spoken_languages": [
                            {
                              "english_name": "Japanese",
                              "iso_639_1": "ja",
                              "name": "日本語"
                            }
                          ],
                          "status": "Released",
                          "tagline": "행복이 기적처럼 쏟아진다",
                          "title": "이웃집 토토로",
                          "video": false,
                          "vote_average": 8.068,
                          "vote_count": 7731
                        }
                        """, APPLICATION_JSON));

        //when
        var actual = adapter.findById(contentId);

        //then
        assertThat(actual).hasNoNullFieldsOrProperties();
    }

    private RequestMatcher requestTo(String path, Object... uriVars) {
        return requestToUriTemplate(format("https://api.themoviedb.org/3/%s", path), uriVars);
    }
}