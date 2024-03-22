package com.pancake.api.watch.infra;

import com.pancake.api.content.Builders;
import com.pancake.api.content.domain.Content;
import com.pancake.api.content.domain.Playback;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static com.pancake.api.content.Builders.aMetadata;
import static com.pancake.api.content.Builders.aPlayback;
import static com.pancake.api.content.domain.Platform.NETFLIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest
class JpaPlaybackQueryRepositoryTest {

    @Autowired
    JpaPlaybackQueryRepository repository;

    @Autowired
    TestEntityManager em;

    Statistics statistics;

    @BeforeEach
    void setUp() {
        statistics = em.getEntityManager().unwrap(Session.class)
                .getSessionFactory()
                .getStatistics();
        statistics.setStatisticsEnabled(true);
        statistics.clear();
    }

    @Test
    void findByContentIdAndPlatform() {
        //given
        var content = contentWith(aPlayback()
                .platform(NETFLIX)
                .url("https://www.netflix.com/watch/12321"));
        em.persist(content);

        em.flush();
        em.clear();
        statistics.clear();

        //when
        var actual = repository.findByContentIdAndPlatform(content.getId(), NETFLIX);

        //then
        assertAll(
                () -> assertThat(actual)
                        .as("재생 정보가 조회된다.").isPresent().get()
                        .returns("https://www.netflix.com/watch/12321", Playback::getUrl),
                () -> assertThat(statistics.getPrepareStatementCount())
                        .as("select 쿼리가 1번 날아간다.").isOne()
        );
    }

    private Content contentWith(Builders.PlaybackBuilder playback) {
        var content = aMetadata().build().toContent();
        content.add(playback.build());

        return content;
    }
}