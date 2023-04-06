package com.pancake.api.content.infra;

import com.pancake.api.content.domain.Content;
import com.pancake.api.helper.MemoryRepository;

public class MemoryContentRepository extends MemoryRepository<Content> implements ContentRepository {
}
