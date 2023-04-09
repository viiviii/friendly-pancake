package com.pancake.api.content;

public enum NetflixConstant {

    PONYO("https://www.netflix.com/watch/70106454?trackId=255824129", "벼랑 위의 포뇨"),
    TOTORO("https://www.netflix.com/watch/60032294?trackId=254245392", "이웃집 토토로");

    public final String URL;
    public final String TITLE;

    NetflixConstant(String url, String title) {
        this.URL = url;
        this.TITLE = title;
    }
}
