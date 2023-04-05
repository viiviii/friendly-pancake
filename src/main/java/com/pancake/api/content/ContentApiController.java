package com.pancake.api.content;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/contents")
public class ContentApiController {

    @PostMapping
    public ResponseEntity<ContentResponse> save(@RequestBody ContentRequest request) {
        return ResponseEntity.status(CREATED).body(new ContentResponse(1L));
    }
}
