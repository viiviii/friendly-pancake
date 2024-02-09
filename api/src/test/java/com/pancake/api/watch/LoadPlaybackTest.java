package com.pancake.api.watch;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(LoadPlayback.class)
class LoadPlaybackTest {

    @Autowired
    LoadPlayback loadPlayback;

    @DisplayName("존재하지 않는 아이디로 조회 시 예외가 발생한다")
    @Test
    void getThrownExceptionWhenContentNotExist() {
        //given
        var nonExistingId = 999L;

        //when
        ThrowingCallable actual = () -> loadPlayback.query(nonExistingId);

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }
}