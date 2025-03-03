package workshop.springai.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.model.Media;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ImageRecognitionService {

    private final ChatClient chatClient;

    public ImageRecognitionService(@Qualifier("chatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String processImage(MultipartFile file) throws IOException {
        log.info("image recognition service started processing: {}", file.getOriginalFilename());

        Resource image = new InputStreamResource(file.getInputStream());
        log.info("image recognition service: {} file", image.getFilename());
        Media myMedia = new Media(MimeTypeUtils.IMAGE_PNG, image);
        Message userMessage = new UserMessage("""
                What is this image? \
                Moreover, generate a list of a maximum of 70 comma-separated tags for categorizing it on FLickr. \
                You must use double quotes only if the tag has more than one word""", List.of(myMedia));
        Message systemMessage = new SystemMessage("Use the following format to answer the question: <answer> description. Tags: images tags");
        return chatClient.prompt().messages(List.of(userMessage, systemMessage)).call().content();

    }
}
