
<img src="images/img.png"  alt="Talent Arena" height="173"/>
<img src="images/bcn-jug.png" alt="Barcelona JUG" height="173"/>

# Workshop: Enhancing Java Applications with Spring AI

## Project Requirements
- [Java](https://sdkman.io/) 21+
- [Maven](https://maven.apache.org/download.cgi) 3.6+
- [Docker](https://www.docker.com/)
- [curl](https://curl.se/) or any other REST client ([Postman](https://www.postman.com/), [HTTPie](https://httpie.io/), [VS code Rest client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client), [Jetbrains Http Client](https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html))
- An [OpenAI API](https://platform.openai.com/docs/overview) key or [Ollama](https://ollama.com/) installed locally
  

## Exercise 4—image recognition

1. Create a service `ImageRecognitionService` with the following method:
* `processImage`, receiving a multipart file and returning a `String` with the image description.
* Create a `Resource` from the input stream.
* Convert the `Resource` to a `Media`.
* The prompt needs to pass the image as part of the `UserMessage` media.
* Include a `SystemMessage` giving instructions on thw answer format.
* Pass the `UserMessage` and `SystemMessage` to the `chatClient` as part of the **messages** and return the string response.

<details>
<summary>ImageRecognition service — Code example</summary>

### ImageRecognition service
```java
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
        log.info("image recognition service: {} file type", image.getFilename());
        Media myMedia = new Media(MimeTypeUtils.IMAGE_JPEG, image);
        Message userMessage = new UserMessage("What is this image?", List.of(myMedia));
        Message systemMessage = new SystemMessage("Use the following format to answer the question: <image> <question> <answer>");
        return chatClient.prompt().messages(List.of(userMessage, systemMessage)).call().content();

    }
}
```
</details>

2. Create a controller `ImageRecognitionController` with the following methods:
  * `processImage`, receiving a multipart file as a `@RequestParam` and returning a `ResponseEntity<String>` with the image description.

<details>
<summary>ImageRecognition controller — Code example</summary>

### ImageRecognition controller

```java
   package workshop.springai.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import workshop.springai.services.ImageRecognitionService;

@Slf4j
@RestController
public class ImageRecognitionController {

    private final ImageRecognitionService imageRecognitionService;

    public ImageRecognitionController(ImageRecognitionService imageRecognitionService) {
        this.imageRecognitionService = imageRecognitionService;
    }

    @PostMapping(value = "/image/recognition", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> processImage(@RequestParam("file") MultipartFile file) {
        log.info("Image processing: process started");
        try {
            return ResponseEntity.ok(imageRecognitionService.processImage(file));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Image processing: process failed" + e.getMessage());
        }
    }
}
```
</details>
3. Create a directory of `images` at the project's root and copy these [images](https://github.com/k8s-spring-ai-workshop/talent-arena/tree/exercise4-image-recognition/images) there.

4. Use your favorite HTTP client and make a POST request to the `/image/recognition` endpoint with a multipart file containing an image. The response should be a string with the image description.

#### HTTP request
```http request
POST http://localhost:8080/image/recognition
Content-Type: multipart/form-data;boundary=WebAppBoundary

--WebAppBoundary
Content-Disposition: form-data; name="file"; filename="bcn-jug.png"

< images/bcn-jug.png
--WebAppBoundary--  
```

#### curl
```shell
curl -X POST http://localhost:8080/image/recognition \
  -H "Content-Type: multipart/form-data" \
  -F "file=@images/bcn-jug.png"
```
