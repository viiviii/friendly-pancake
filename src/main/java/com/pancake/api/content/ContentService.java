package com.pancake.api.content;

import org.springframework.stereotype.Service;

@Service
public class ContentService {
    public Content save(ContentRequest request) {
        return new Content(1L);
    }
}
