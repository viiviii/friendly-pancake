package com.pancake.api.watch.infra;

import com.pancake.api.content.Builders;
import com.pancake.api.content.domain.Content;
import com.pancake.api.watch.domain.WatchContentRepository;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static com.pancake.api.content.Builders.aMetadata;
import static com.pancake.api.content.Builders.aPlayback;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest
@Import(WatchContentQueryRepository.class)
class WatchContentQueryRepositoryTest {

    @Autowired
    WatchContentRepository repository;

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
    void findAll() {
        //given
        em.persist(contentWith(aPlayback()));
        em.persist(contentWith(aPlayback()));
        em.persist(contentWith(aPlayback()));

        em.flush();
        em.clear();
        statistics.clear();

        //when
        var actual = repository.findAll();

        //then
        assertThat(actual).hasSize(3);
        assertAll(
                () -> assertThat(statistics.getCollectionLoadCount())
                        .as("3개의 컬렉션이 조회된다").isEqualTo(3),
                () -> assertThat(statistics.getCollectionFetchCount())
                        .as("컬렉션을 1번 가져온다").isEqualTo(1),
                () -> assertThat(statistics.getPrepareStatementCount())
                        .as("select 쿼리가 1+1번 날아간다.").isEqualTo(1 + 1)
        );
    }

    private Content contentWith(Builders.PlaybackBuilder playback) {
        var content = aMetadata().build().toContent();
        content.add(playback.build());

        return content;
    }
}