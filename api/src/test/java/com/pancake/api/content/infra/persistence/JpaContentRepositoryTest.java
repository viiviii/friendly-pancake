package com.pancake.api.content.infra.persistence;

import com.pancake.api.content.domain.Content;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static com.pancake.api.content.Builders.aContentSaveCommand;
import static com.pancake.api.content.Builders.aPlayback;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest
class JpaContentRepositoryTest {

    @Autowired
    JpaContentRepository repository;

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
    void 자식_엔티티_추가_시_insert_쿼리만_발생한다() {
        //given
        var content = repository.save(content());
        statistics.clear();

        //when
        content.add(aPlayback().build());
        em.flush();

        //then
        assertAll(
                () -> assertThat(statistics.getEntityInsertCount())
                        .as("실행된 insert 쿼리는 1개이다").isOne(),
                () -> assertThat(statistics.getPrepareStatementCount())
                        .as("실행된 sql 쿼리는 1개이다").isOne()
        );
    }

    private Content content() {
        return aContentSaveCommand().build().toContent();
    }
}
