package com.pancake.api.bookmark;

import org.springframework.data.repository.Repository;

interface JpaBookmarkRepository extends Repository<Bookmark, Long>, BookmarkRepository {
}
