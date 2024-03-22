package com.pancake.api.content.domain;

public enum Platform {
    NETFLIX("넷플릭스", "https://www.netflix.com/watch/"),
    DISNEY_PLUS("디즈니플러스", "https://www.disneyplus.com/video/");

    private final String label;
    private final Url baseUrl;

    Platform(String label, String baseUrl) {
        this.label = label;
        this.baseUrl = new Url(baseUrl);
    }

    public String label() {
        return this.label;
    }

    public String baseUrl() {
        return this.baseUrl.toString();
    }
}