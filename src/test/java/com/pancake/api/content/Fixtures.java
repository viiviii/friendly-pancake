package com.pancake.api.content;

import com.pancake.api.content.application.dto.ContentRequest;
import com.pancake.api.content.application.dto.ContentResponse;
import com.pancake.api.content.domain.Content;

public interface Fixtures {

    long NOT_EXISTS_ID = Long.MAX_VALUE;

    enum Netflix {

        PONYO("https://www.netflix.com/watch/70106454?trackId=255824129", "벼랑 위의 포뇨",
                """
                        따분한 바다 생활이 싫어 가출한 물고기 공주 포뇨. 벼랑 위에 사는 인간 꼬마 소스케를 만나 친구가 된다.
                        온 바다가 들썩들썩 포뇨를 찾아 나서지만, 이 고집불통 물고기의 소원은 오직 하나. 포뇨도 소스케처럼 인간이 될 거야!""",
                "https://occ-0-1360-2218.1.nflxso.net/dnm/api/v6/E8vDc"),
        TOTORO("https://www.netflix.com/watch/60032294?trackId=254245392",
                "이웃집 토토로",
                "엄마의 입원으로 두 어린 자매는 아빠와 함께 일본의 한 시골 마을에서 여름을 보내게 된다.",
                "https://occ-0-1360-2218.1.nflxso.net/dnm/api/v6/E8vDc");

        public final ContentRequest REQUEST;
        public final Content CONTENT;
        public final ContentResponse RESPONSE;

        Netflix(String url, String title, String description, String imageUrl) {
            this.REQUEST = new ContentRequest(url, title, description, imageUrl);
            this.CONTENT = REQUEST.toEntity();
            this.RESPONSE = ContentResponse.fromEntity(CONTENT);
        }
    }

}