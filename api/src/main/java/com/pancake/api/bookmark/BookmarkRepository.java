package com.pancake.api.bookmark;

import java.util.List;

public interface BookmarkRepository {

    Bookmark save(Bookmark bookmark);

    List<Bookmark> findAll();
}
