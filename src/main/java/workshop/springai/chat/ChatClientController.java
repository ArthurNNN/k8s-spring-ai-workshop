package workshop.springai.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ChatClientController {

    private final ChatClient chatClient;

    @Autowired
    public ChatClientController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping(value = "/chat", produces = "text/plain", consumes = "application/json")
    String chat(@RequestBody(required = true) String message) {

        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }


}
