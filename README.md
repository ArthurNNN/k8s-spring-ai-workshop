
<img src="images/img.png"  alt="Talent Arena" height="173"/>
<img src="images/bcn-jug.png" alt="Barcelona JUG" height="173"/>

# Workshop: Enhancing Java Applications with Spring AI

## Project Requirements
- [Java](https://sdkman.io/) 21+
- [Maven](https://maven.apache.org/download.cgi) 3.6+
- [Docker](https://www.docker.com/)
- [curl](https://curl.se/) or any other REST client ([Postman](https://www.postman.com/), [HTTPie](https://httpie.io/), [VS code Rest client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client), [Jetbrains Http Client](https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html))
- An [OpenAI API](https://platform.openai.com/docs/overview) key or [Ollama](https://ollama.com/) installed locally


## Exercise 1—chatbot

1. Create your spring boot application using the [Spring Initializr](https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.4.3&packaging=jar&jvmVersion=21&groupId=workshop.springai&artifactId=talent-arena&name=talent-arena&description=Demo%20project%20for%20Spring%20Boot&packageName=workshop.springai&dependencies=web,spring-ai-openai,actuator,lombok).
This link will create a project with the following dependencies:
  * Spring Web
  * Spring AI OpenAI
  * Spring Boot Actuator
  * Lombok

And the following configuration:
  * Java 21
  * Spring Boot 3.4.3
  * Spring AI 1.0.0-M6
  * Maven
2. Click on the `Generate` button to download the project.
3. Unzip the project and open it in your favorite IDE.
4. Create a `.env.local` file in the root of the project with the following content:
```properties
API_KEY='YOUR_OPEN_AI_KEY'
```
5. Create an [OpenAI key](https://platform.openai.com/settings/organization/api-keys) in your account and add it to the `.env.local` file.
6. Change the `application.properties` file to use the OpenAI API key and the GPT-4o-mini model:
```properties
spring.application.name=talent-arena

#import variables from .env.local file as properties
spring.config.import=file:.env.local[.properties]

# Properties for the OpenAI API
spring.ai.openai.api-key=${API_KEY}
spring.ai.openai.chat.options.model=gpt-4o-mini

logging.level.org.springframework.ai.chat.client.advisor=INFO
```
7. Create a `ChatClientConfig` class with the following content:
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
8. Create a `ChatController` class with the following content:
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
12. Open a new terminal and test the chatbot using curl, asking about the previous answer:
```shell
curl -X GET http://localhost:8080/chat -H "Content-Type: text/plain" -d "Could you repeat the previous answer ?"
```
<details>
<summary>Optional - running the Application with Ollama and DeepSeek</summary>

13. Install [Ollama](https://ollama.com/download) in your local machine.

14. Add the following properties inside the `application.properties` file to use Ollama with the DeepSeek model:
```properties
# Properties for the Ollama API
spring.ai.ollama.init.pull-model-strategy=always
spring.ai.ollama.chat.options.model=deepseek-r1:1.5b
```
15. Change the `pom.xml` to add the new dependency `spring-ai-ollama-spring-boot-starter` in a 
specific maven profile and the `spring-ai-openai-spring-boot-starter` in a default maven profile.

The final version of the `pom.xml` should look like this:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.3</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>workshop.springai</groupId>
    <artifactId>talent-arena</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>talent-arena</name>
    <description>Demo project for Spring Boot</description>
    <url/>
    <licenses>
        <license/>
    </licenses>
    <developers>
        <developer/>
    </developers>
    <scm>
        <connection/>
        <developerConnection/>
        <tag/>
        <url/>
    </scm>
    <properties>
        <java.version>21</java.version>
        <spring-ai.version>1.0.0-M6</spring-ai.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>${spring-ai.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

    <profiles>
        <profile>
            <id>openai</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <dependencies>
                <dependency>
                    <groupId>org.springframework.ai</groupId>
                    <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
                </dependency>
            </dependencies>
        </profile>

        <profile>
            <id>ollama</id>
            <dependencies>
                <dependency>
                    <groupId>org.springframework.ai</groupId>
                    <artifactId>spring-ai-ollama-spring-boot-starter</artifactId>
                </dependency>
            </dependencies>
        </profile>

    </profiles>

</project>

```
16. Build the project using Maven with the Ollama profile:
```shell
mvn clean install -Pollama
```
17. Run the project using Maven with the Ollama profile:
```shell
mvn spring-boot:run -Pollama
```
18. Open a new terminal and test the chatbot using curl:
```shell
curl -X GET http://localhost:8080/chat -H "Content-Type: text/plain" -d "What is the capital of Brazil ?"
```
19. Open a new terminal and test the chatbot using curl, asking about the previous answer:
```shell
curl -X GET http://localhost:8080/chat -H "Content-Type: text/plain" -d "Could you repeat the previous answer ?"
```
</details>

## Exercise 2—chatbot with memory

1. Open the `ChatClientConfig` class and add the following bean methods:
```java
    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    @Bean(name = "chatClientWithMemory")
    public ChatClient chatClientWithMemory(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        return chatClientBuilder
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new SimpleLoggerAdvisor())
                .build();
    }
```
2. Create a `ChatMemoryController` class with the following content:
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
public class ChatMemoryController {

    private final ChatClient chatClient;

    public ChatMemoryController(@Qualifier("chatClientWithMemory") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping(value = "/chat/memory", produces = "text/plain", consumes = "text/plain")
    String chat(@RequestBody String message) {
        log.info("Chat Memory with message: {}", message);

        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

}
```
3. Build the project using Maven:
```shell
mvn clean install
```
4. Run the project using Maven:
```shell
mvn spring-boot:run
```
5. Open a new terminal and test the chatbot using curl:
```shell
curl -X GET http://localhost:8080/chat/memory -H "Content-Type: text/plain" -d "What is the capital of Brazil ?"
```
6. Open a new terminal and test the chatbot using curl, asking about the previous answer:
```shell
curl -X GET http://localhost:8080/chat/memory -H "Content-Type: text/plain" -d "Could you repeat the previous answer ?"
```
<details>
<summary>Optional - running the Application with Ollama and DeepSeek</summary>

7. Install [Ollama](https://ollama.com/download) in your local machine.

8. Add the following properties inside the `application.properties` file to use Ollama with the DeepSeek model:
```properties
# Properties for the Ollama API
spring.ai.ollama.init.pull-model-strategy=always
spring.ai.ollama.chat.options.model=deepseek-r1:1.5b
```
9. Change the `pom.xml` to add the new dependency `spring-ai-ollama-spring-boot-starter` in a
    specific maven profile and the `spring-ai-openai-spring-boot-starter` in a default maven profile.

The final version of the `pom.xml` should look like this:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.3</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>workshop.springai</groupId>
    <artifactId>talent-arena</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>talent-arena</name>
    <description>Demo project for Spring Boot</description>
    <url/>
    <licenses>
        <license/>
    </licenses>
    <developers>
        <developer/>
    </developers>
    <scm>
        <connection/>
        <developerConnection/>
        <tag/>
        <url/>
    </scm>
    <properties>
        <java.version>21</java.version>
        <spring-ai.version>1.0.0-M6</spring-ai.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>${spring-ai.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

    <profiles>
        <profile>
            <id>openai</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <dependencies>
                <dependency>
                    <groupId>org.springframework.ai</groupId>
                    <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
                </dependency>
            </dependencies>
        </profile>

        <profile>
            <id>ollama</id>
            <dependencies>
                <dependency>
                    <groupId>org.springframework.ai</groupId>
                    <artifactId>spring-ai-ollama-spring-boot-starter</artifactId>
                </dependency>
            </dependencies>
        </profile>

    </profiles>

</project>

```
10. Build the project using Maven with the Ollama profile:
```shell
mvn clean install -Pollama
```
11. Run the project using Maven with the Ollama profile:
```shell
mvn spring-boot:run -Pollama
```
12. Open a new terminal and test the chatbot using curl:
```shell
curl -X GET http://localhost:8080/chat/memory -H "Content-Type: text/plain" -d "What is the capital of Brazil ?"
```
13. Open a new terminal and test the chatbot using curl, asking about the previous answer:
```shell
curl -X GET http://localhost:8080/chat/memory -H "Content-Type: text/plain" -d "Could you repeat the previous answer ?"
```
</details>

## Exercise 3—chatbot with RAG

1. Open the `pom.xml` and add the following dependencies:
```xml
<dependencies>    
    <!-- Spring AI RAG dependency - to process PDFs, DOCs, HTMLs -->
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-tika-document-reader</artifactId>
    </dependency>
    <!-- Spring AI RAG dependency - to work with PG Vector Database -->
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-pgvector-store-spring-boot-starter</artifactId>
    </dependency>
    <!-- Dependency to start-up the PG Vector Database using docker-compose -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-docker-compose</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```
2. Create a `compose.yml` file in the root of the project with the following content:
```yaml
services:
  database:
    image: 'pgvector/pgvector:pg16'
    ports:
      - '5432'
    environment:
      - 'POSTGRES_USER=myuser'
      - 'POSTGRES_DB=mydatabase'
      - 'POSTGRES_PASSWORD=secret'
    labels:
      org.springframework.boot.service-connection: postgres
```
3. Add the following properties inside the `application.properties` file, to create the PG Vector Schema at initialization 
and the Talent Arena websites that will be used during the Ingestion process of the RAG:
```properties
#PostgreSQL vector database properties
spring.ai.vectorstore.pgvector.initialize-schema=true

# Talent Arena properties
talent-arena.websites.workshops=https://talentarena.tech/workshops-agenda/
talent-arena.websites.talks=https://talentarena.tech/talks-agenda/
talent-arena.websites.conference=https://talentarena.tech/conference-agenda/

# Spring Boot docker-compose properties
spring.docker.compose.stop.command=down
```
4. Create the following Configuration properties classes in the `workshop.springai.config` package:
```java
package workshop.springai.config;

public record Website(String url) {
}
```
```java
package workshop.springai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("talent-arena")
public record TalentArenaProperties(Map<String, Website> websites) {
}
```
5. Add the following annotation `@EnableConfigurationProperties(TalentArenaProperties.class)` to the `TalentArenaApplication` class,
the final version of the class should look like this:
```java
package workshop.springai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import workshop.springai.config.TalentArenaProperties;

@EnableConfigurationProperties({TalentArenaProperties.class})
@SpringBootApplication
public class TalentArenaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TalentArenaApplication.class, args);
    }

}
```

6. Open the `ChatClientConfig` class and add the following bean methods:
```java
    @Bean
    public TextSplitter textSplitter() {
        return new TokenTextSplitter();
    }

    @Bean(name = "chatClientWithRag")
    public ChatClient chatClientWithRag(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        return chatClientBuilder
                .defaultAdvisors(
                        new QuestionAnswerAdvisor(vectorStore),
                        new SimpleLoggerAdvisor())
                .build();
    }
```
7. Create a `IngestionService` class with the following content, to ingest the PDF files from the resources folder into the PG Vector Database:
```java
package workshop.springai.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import workshop.springai.config.TalentArenaProperties;

@Slf4j
@Service
public class IngestionService implements CommandLineRunner {

    private final VectorStore vectorStore;
    private final TextSplitter textSplitter;
    private final TalentArenaProperties talentArenaProperties;

    public IngestionService(VectorStore vectorStore, TextSplitter textSplitter,
                            TalentArenaProperties talentArenaProperties) {
        this.vectorStore = vectorStore;
        this.textSplitter = textSplitter;
        this.talentArenaProperties = talentArenaProperties;
    }

    @Override
    public void run(String... args) {
        log.info("Ingesting data for Talent Arena");

        talentArenaProperties.websites().forEach((key, website) -> {
            log.info("Ingesting data for {} with the url {}", key, website.url());
            TikaDocumentReader textReader = new TikaDocumentReader(website.url());
            var documents = textSplitter.apply(textReader.get());
            vectorStore.accept(documents);
        });

        log.info("VectorStore Loaded with data!");
    }
}

```
8. Create a `RagController` class with the following content:
```java
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
curl -X GET http://localhost:8080/chat/rag/talent-arena/ -H "Content-Type: text/plain" -d "What are the content related to Artificial Intelligence in Talent Arena 2025 ?"
```
<details>
<summary>Optional - running the Application with Ollama and DeepSeek</summary>

12. Install [Ollama](https://ollama.com/download) in your local machine.

13. Add the following properties inside the `application.properties` file to use Ollama with the DeepSeek model:
```properties
# Properties for the Ollama API
spring.ai.ollama.init.pull-model-strategy=always
spring.ai.ollama.init.embedding.additional-models=mxbai-embed-large
spring.ai.ollama.chat.options.model=deepseek-r1:1.5b
```
14. Change the `pom.xml` to add the new dependency `spring-ai-ollama-spring-boot-starter` in a
   specific maven profile and the `spring-ai-openai-spring-boot-starter` in a default maven profile.

The final version of the `pom.xml` should look like this:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.3</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>workshop.springai</groupId>
    <artifactId>talent-arena</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>talent-arena</name>
    <description>Demo project for Spring Boot</description>
    <url/>
    <licenses>
        <license/>
    </licenses>
    <developers>
        <developer/>
    </developers>
    <scm>
        <connection/>
        <developerConnection/>
        <tag/>
        <url/>
    </scm>
    <properties>
        <java.version>21</java.version>
        <spring-ai.version>1.0.0-M6</spring-ai.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <!-- Spring AI RAG dependency - to process PDFs, DOCs, HTMLs -->
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-tika-document-reader</artifactId>
        </dependency>
        <!-- Spring AI RAG dependency - to work with PG Vector Database -->
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-pgvector-store-spring-boot-starter</artifactId>
        </dependency>
        <!-- Dependency to start-up the PG Vector Database using docker-compose -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-docker-compose</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>${spring-ai.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

    <profiles>
        <profile>
            <id>openai</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <dependencies>
                <dependency>
                    <groupId>org.springframework.ai</groupId>
                    <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
                </dependency>
            </dependencies>
        </profile>

        <profile>
            <id>ollama</id>
            <dependencies>
                <dependency>
                    <groupId>org.springframework.ai</groupId>
                    <artifactId>spring-ai-ollama-spring-boot-starter</artifactId>
                </dependency>
            </dependencies>
        </profile>

    </profiles>

</project>

```
15. Build the project using Maven with the Ollama profile:
```shell
mvn clean install -Pollama
```
16. Run the project using Maven with the Ollama profile:
```shell
mvn spring-boot:run -Pollama
```
17. Open a new terminal and test the chatbot using curl:
```shell
curl -X GET http://localhost:8080/chat/rag/talent-arena/ -H "Content-Type: text/plain" -d "What are the content related to Artificial Intelligence in Talent Arena 2025 ?"
```
</details>
<details>
<summary>Optional - Start the PGVector database with testContainer instead of docker-compose</summary>

18. Open the `pom.xml` and add the following dependencies:
```xml
<dependencies>    
    <!-- TestContainers dependencies -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-testcontainers</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-spring-boot-testcontainers</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>postgresql</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```
19. Create a `TestContainersConfiguration` class with the following content, in the `src/test/java` folder in the `workshop.springai.config` package:
```java
package workshop.springai.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestContainersConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> pgvectorContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("pgvector/pgvector:pg16"));
    }

}

```

20. Create a `TestTalentArenaApplication` class with the following content, in the `src/test/java` folder in the `workshop.springai` package:
```java
package workshop.springai;

import org.springframework.boot.SpringApplication;
import workshop.springai.config.TestContainersConfiguration;

public class TestTalentArenaApplication {

    public static void main(String[] args) {
        SpringApplication.from(TalentArenaApplication::main)
                .with(TestContainersConfiguration.class)
                .run(args);
    }

}
```
21. Add the following annotation `@Import(TestContainersConfiguration.class)` to the `TalentArenaApplicationTests` class, the final version of the class should look like this:
```java
package workshop.springai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import workshop.springai.config.TestContainersConfiguration;

@Import(TestContainersConfiguration.class)
@SpringBootTest
class TalentArenaApplicationTests {

    @Test
    void contextLoads() {
    }

}

```
22. Build the project using Maven:
```shell
mvn clean install
```
23. Run the project using Maven, but this time with test-run:
```shell
mvn spring-boot:test-run
```
24. Open a new terminal and test the chatbot using curl:
```shell
curl -X GET http://localhost:8080/chat/rag/talent-arena/ -H "Content-Type: text/plain" -d "What are the content related to Artificial Intelligence in Talent Arena 2025 ?"
```
</details>

## Exercise 4—image recognition

1. Create an [OpenAI key](https://platform.openai.com/settings/organization/api-keys) in your account and add it to the `.env.local` file.
2. Create a controller `ImageRecognitionController` with the following methods:
  * `processImage`, receiving a multipart file as a `@RequestParam` and returning a `ResponseEntity<String>` with the image description.

<details>
<summary>ImageRecognition controller — Code example</summary>

### ImageRecognition controller

```java
    @PostMapping(value = "/image/recognition", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> processImage(@RequestParam("file") MultipartFile file) {
        log.info("Image processing: process started");
        try {
            return ResponseEntity.ok(imageRecognitionService.processImage(file));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Image processing: process failed" + e.getMessage());
        }
    }
```
</details>

3. Create a service `ImageRecognitionService` with the following method:
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
    public String processImage(MultipartFile file) throws IOException {
        log.info("image recognition service started processing: {}", file.getOriginalFilename());

        Resource image = new InputStreamResource(file.getInputStream());
        log.info("image recognition service: {} file type", image.getFilename());
        Media myMedia = new Media(MimeTypeUtils.IMAGE_JPEG, image);
        Message userMessage = new UserMessage("What is this image?", List.of(myMedia));
        Message systemMessage = new SystemMessage("Use the following format to answer the question: <image> <question> <answer>");
        return chatClient.prompt().messages(List.of(userMessage, systemMessage)).call().content();

    }
```
</details>
4. Use your favorite HTTP client and make a POST request to the `/image/recognition` endpoint with a multipart file containing an image. The response should be a string with the image description.

#### HTTP request
```http request
POST http://localhost:8080/image/recognition
Content-Type: multipart/form-data; boundary=WebAppBoundary

--WebAppBoundary
Content-Disposition: form-data; name="file"; filename="image.jpg"

< /Users/esriva00/Pictures/bzrYBxvm_400x400.jpg
--WebAppBoundary--
```

#### curl
```shell
curl -X POST --location "http://localhost:8080/image/recognition" \
    -H "Content-Type: multipart/form-data; boundary=WebAppBoundary" \
    -F "file=@/Users/esriva00/Pictures/bzrYBxvm_400x400.jpg;filename=image.jpg;type=*/*"
```