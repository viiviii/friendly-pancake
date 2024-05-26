package com.pancake.api.content.infra.persistence;

import com.pancake.api.content.domain.Content;
import com.pancake.api.content.domain.ContentRepository;
import org.springframework.data.repository.Repository;

interface JpaContentRepository extends Repository<Content, Long>, ContentRepository {
}
