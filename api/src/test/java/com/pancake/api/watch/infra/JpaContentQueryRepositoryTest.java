package com.pancake.api.watch.infra;

import com.pancake.api.content.Builders;
import com.pancake.api.content.domain.Content;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static com.pancake.api.content.Builders.aMetadata;
import static com.pancake.api.content.Builders.aPlayback;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest
class JpaContentQueryRepositoryTest {

    @Autowired
    JpaContentQueryRepository repository;

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
        assertAll(
                () -> assertThat(actual)
                        .as("3개의 컨텐츠가 조회되고 각각 1개의 재생 정보를 가지고 있다.")
                        .hasSize(3)
                        .allSatisfy(e -> assertThat(e.getPlaybacks()).hasSize(1)),
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