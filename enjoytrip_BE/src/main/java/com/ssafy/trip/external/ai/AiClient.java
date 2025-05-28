package com.ssafy.trip.external.ai;

import com.ssafy.trip.domain.tripplan.enums.TripStyle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiClient {
    private final ChatClient simpleChatClient;

    public Object simpleGeneration(String userInput) {
        var spec = simpleChatClient.prompt()
                .system(t -> t.param("language", "korean")
                        .param("character", "chill"))
                .user(userInput)
                .call();
        return spec.content();
    }
    
    public List<TripStyle> tripStyleGeneration(String tripContent) {
        PromptTemplate pt = new PromptTemplate("""
                {tripContent} 관련된 여행 스타일을 분류해줘
                RELAXING,SIGHTSEEING,NATURE,CULTURAL,FOODIE,ADVENTURE,SHOPPING,PHOTO
                이것 중에 골라줘. 1~2개 정도 골라줘
                """);
        Prompt prompt = pt.create(Map.of("tripContent", tripContent));
        var spec = simpleChatClient.prompt(prompt)
                .system(t -> t.param("language", "korean").param("character", "general"))
                .call()
                .entity(new ParameterizedTypeReference<List<TripStyle>>() {
                });
        return spec;
    }

}
