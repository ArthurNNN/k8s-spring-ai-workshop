package workshop.springai.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ChatClientController {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    @Autowired
    public ChatClientController(ChatClient chatClient, ChatMemory chatMemory) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
    }

    @GetMapping(value = "/chat", produces = "text/plain", consumes = "application/json")
    String chat(@RequestBody String message) {
        log.info("Chatting with message: {}", message);

        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    @GetMapping(value = "/chat/with/memory", produces = "text/plain", consumes = "application/json")
    String chatWithMemory(@RequestBody String message) {
        log.info("Chatting Memory with message: {}", message);

        return chatClient.prompt()
                .user(message)
                .advisors(new MessageChatMemoryAdvisor(chatMemory))
                .call()
                .content();
    }


}
