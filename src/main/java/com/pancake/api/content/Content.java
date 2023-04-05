package com.pancake.api.content;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Content {
    private Long id;
    private String url;
    private String title;


    public Long id() {
        return this.id;
    }

    public String url() {
        return this.url;
    }

    public String title() {
        return this.title;
    }
}
