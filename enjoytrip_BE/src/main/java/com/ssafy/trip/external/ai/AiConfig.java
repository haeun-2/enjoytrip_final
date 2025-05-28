package com.ssafy.trip.external.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import redis.clients.jedis.JedisPooled;

@Configuration
public class AiConfig {
    @Value("${ssafy.ai.system-prompt}")
    String systemPrompt;

    @Bean
    ChatClient simpleChatClient(ChatClient.Builder builder) {
        return builder.defaultSystem(systemPrompt).build();
    }

    @Bean
    ChatClient reReadingChatClient(ChatClient.Builder builder) {
        return builder.defaultSystem(systemPrompt)
                      .defaultAdvisors(new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE))
                      .build();
    }

    @Bean
    ChatMemoryRepository chatMemoryRepository() {
        return new InMemoryChatMemoryRepository();
    }

     @Bean
    ChatClient advisedChatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
        return builder.defaultSystem(systemPrompt)
                .defaultAdvisors(new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1))
                .build();
    }

//    @Bean
    VectorStore vectorStore(EmbeddingModel embeddingModel,
                            @Value("${spring.ai.vectorstore.redis.index}") String index,
                            @Value("${spring.ai.vectorstore.redis.prefix}") String prefix) {
        return RedisVectorStore.builder(new JedisPooled("localhost", 6379), embeddingModel) // Replace null with your embedding model
                .indexName(index)
                .prefix(prefix)
                .metadataFields(
                        RedisVectorStore.MetadataField.tag("category"),
                        RedisVectorStore.MetadataField.numeric("meta_num"),
                        RedisVectorStore.MetadataField.text("meta_txt")
                )
                .initializeSchema(true)
                .build();
    }
}
