
<img src="images/img.png"  alt="Talent Arena" height="173"/>
<img src="images/bcn-jug.png" alt="Barcelona JUG" height="173"/>

# Workshop: Enhancing Java Applications with Spring AI

## Project Requirements
- Java 21+
- Maven 3.6+
- docker
- curl or any other REST client
- An OpenAI API key


## Exercise 1—chatbot

1. Create your spring boot application using the [Spring Initializr](https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.4.3&packaging=jar&jvmVersion=21&groupId=workshop.springai&artifactId=talent-arena&name=talent-arena&description=Demo%20project%20for%20Spring%20Boot&packageName=workshop.springai&dependencies=web,spring-ai-openai,actuator,lombok).
2. Click on the `Generate` button to download the project.
3. Unzip the project and open it in your favorite IDE.
4. Create a `.env.local` file in the root of the project with the following content:
```properties
API_KEY='YOUR_OPEN_AI_KEY'
```
5. Create an [OpenAI key](https://platform.openai.com/settings/organization/api-keys) in your account and add it to the `.env.local` file.
6. Change the `application.properties` file to the following content:
```properties
spring.application.name=talent-arena

#import variables from .env.local file as properties
spring.config.import=file:.env.local[.properties]

# Properties for the OpenAI API
spring.ai.openai.api-key=${API_KEY}
spring.ai.openai.chat.options.model=gpt-4o-mini

logging.level.org.springframework.ai.chat.client.advisor=INFO
```
7. Create a ChatClientConfig class with the following content:
```java
package workshop.springai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {
    
    @Bean(name = "chatClient")
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultAdvisors(
                        new SimpleLoggerAdvisor())
                .build();
    }
    
}
```
8. Create a ChatController class with the following content:
```java
package workshop.springai.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(@Qualifier("chatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping(value = "/chat", produces = "text/plain", consumes = "text/plain")
    String chat(@RequestBody String message) {
        log.info("Chat with message: {}", message);

        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

}
```
9. Build the project using Maven:
```shell
mvn clean install
```
10. Run the project using Maven:
```shell
mvn spring-boot:run
```
11. Open a new terminal and test the chatbot using curl:
```shell
curl -X GET http://localhost:8080/chat -H "Content-Type: text/plain" -d "What is the capital of Brazil ?"
```

## Exercise 2—chatbot with memory

TBD

## Exercise 3—chatbot with RAG

TBD

## Exercise 4—image recognition

1. Create an [OpenAI key](https://platform.openai.com/settings/organization/api-keys) in your account and add it to the `.env.local` file.
2. Create a controller `ImageRecognitionController` with the following methods:
  * ProcessImage, receiving a multipart file as a `@RequestParam` and returning a `ResponseEntity<String>` with the image description.
3. Create a service `ImageRecognitionService` with the following method:
  * processImage, receiving a multipart file and returning a `String` with the image description.
  * Create a `Resource` from the input stream.
  * Convert the `Resource` to a `Media`.
  * The prompt needs to pass the image as part of the `UserMessage` media.
  * Include a `SystemMessage` giving instructions on thw answer format.
  * Pass the `UserMessage` and `SystemMessage` to the `chatClient` as part of the **messages** and return the string response.