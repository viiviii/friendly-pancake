package com.pancake.api.watch.infra;

import com.pancake.api.content.domain.Content;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import java.util.List;

interface JpaContentQueryRepository extends Repository<Content, Long> {

    @EntityGraph(attributePaths = {"playbacks"})
    List<Content> findAll();
}
