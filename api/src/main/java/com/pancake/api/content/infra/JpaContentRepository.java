package com.pancake.api.content.infra;

import com.pancake.api.content.domain.Content;
import com.pancake.api.content.domain.ContentRepository;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface JpaContentRepository extends Repository<Content, Long>, ContentRepository {

    Content save(Content content);

    List<Content> findAll();

    Optional<Content> findById(long id);
}
