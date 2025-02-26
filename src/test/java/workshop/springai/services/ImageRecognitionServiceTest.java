package workshop.springai.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageRecognitionServiceTest {

    @Mock
    private ChatClient chatClient;

    private ImageRecognitionService imageRecognitionService;

    @BeforeEach
    void setUp() {
        imageRecognitionService = new ImageRecognitionService(chatClient);
    }

    @Test
    void shouldProcessImageSuccessfully() throws IOException {
        // Given
        MockMultipartFile file = new MockMultipartFile(
            "test-image.jpg",
            "test-image.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );
        ChatClient.ChatClientRequestSpec spec = Mockito.mock(ChatClient.ChatClientRequestSpec.class);
        ChatClient.CallResponseSpec responseSpec = Mockito.mock(ChatClient.CallResponseSpec.class);

        String expectedResponse = "This is a test image";
        when(chatClient.prompt()).thenReturn(spec);
        when(chatClient.prompt().messages(Mockito.anyList())).thenReturn(spec);
        when(spec.call()).thenReturn(responseSpec);
        when(chatClient.prompt().messages(Mockito.anyList()).call().content()).thenReturn(expectedResponse);

        // When
        String result = imageRecognitionService.processImage(file);

        // Then
        assertThat(result)
            .isNotNull()
            .isEqualTo(expectedResponse);
    }
}