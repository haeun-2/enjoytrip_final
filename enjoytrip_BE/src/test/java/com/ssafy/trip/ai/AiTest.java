package com.ssafy.trip.ai;

import com.ssafy.trip.external.ai.AiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class AiTest {
    @Autowired
    AiClient aiClient;

    // @Test
    void simpleChatTest() {
        String userInput = "안녕? 오늘 날씨 어때?";
        Object response = aiClient.simpleGeneration(userInput);

        if (response instanceof ChatResponse result) {
            Usage usage = result.getMetadata().getUsage();
            log.debug("content {}",usage);
        }
        log.debug("response {}", response);
    }

    // @Test
    void tripStyleGenerationTest() {
        String tripContent = "카페, 박물관, 여름";
        var response = aiClient.tripStyleGeneration(tripContent);

        log.debug("response {}", response);
    }
}