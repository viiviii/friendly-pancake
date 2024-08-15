package com.pancake.api.bookmark.infra;

import com.pancake.api.bookmark.domain.Bookmark;
import com.pancake.api.bookmark.domain.BookmarkRepository;
import org.springframework.data.repository.Repository;

interface JpaBookmarkRepository extends Repository<Bookmark, Long>, BookmarkRepository {
}
