package com.treecore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 라이브러리 컨텍스트 로드 테스트
 * 이 테스트는 라이브러리의 Spring 컨텍스트가 정상적으로 로드되는지 확인합니다.
 */
@SpringBootTest(classes = TreecoreApplicationTests.TestApplication.class)
class TreecoreApplicationTests {

    @Test
    void contextLoads() {
        // 컨텍스트 로드 테스트
    }

    /**
     * 테스트용 애플리케이션 설정
     * 실제 애플리케이션이 아닌 테스트용 설정입니다.
     */
    @SpringBootApplication
    static class TestApplication {
        // 테스트용 애플리케이션 설정
    }
} 