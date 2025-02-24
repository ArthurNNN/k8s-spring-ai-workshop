
<img src="images/img.png"  alt="Talent Arena" height="173"/>
<img src="images/bcn-jug.png" alt="Barcelona JUG" height="173"/>

# Workshop: Enhancing Java Applications with Spring AI

Run integration test with OpenAI as default provider with

```shell
mvn spring-boot:test-run
```
```shell
mvn verify
```
Enable Ollama by using the `ollama` profile:

```shell
mvn spring-boot:test-run -Pollama
```
```shell
mvn verify -Pollama
```

## Exercise 1—chatbot

TBD

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