package workshop.springai.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RagController {

    private final ChatClient chatClient;

    public RagController(@Qualifier(value = "chatClientWithRag") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping(value = "/chat/rag/talent-arena/", produces = "text/plain", consumes = "text/plain")
    public String chat(@RequestBody String message) {
        log.info("Chat Rag with message: {}", message);

        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
