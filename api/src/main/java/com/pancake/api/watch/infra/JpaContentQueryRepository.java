package com.pancake.api.watch.infra;

import com.pancake.api.content.domain.Content;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface JpaContentQueryRepository extends Repository<Content, Long> {

    @EntityGraph(attributePaths = {"playbacks"})
    List<Content> findAll();

    Optional<Content> findById(long id);
}
