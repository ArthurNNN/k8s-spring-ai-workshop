package workshop.springai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    @Bean
    public TextSplitter textSplitter() {
        return new TokenTextSplitter();
    }

    @Bean(name = "chatClient")
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        return chatClientBuilder
                .defaultAdvisors(
                        new SimpleLoggerAdvisor())
                .build();
    }

    @Bean(name = "chatClientWithMemory")
    public ChatClient chatClientWithMemory(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        return chatClientBuilder
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new SimpleLoggerAdvisor())
                .build();
    }

    @Bean(name = "chatClientWithRag")
    public ChatClient chatClientWithRag(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        var searchRequest = SearchRequest.builder()
                .similarityThreshold(1)
                .topK(1)
                .build();

        return chatClientBuilder
                .defaultAdvisors(
                        new QuestionAnswerAdvisor(vectorStore, searchRequest),
                        new SimpleLoggerAdvisor())
                .build();
    }


}
