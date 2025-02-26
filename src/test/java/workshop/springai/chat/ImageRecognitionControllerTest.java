package workshop.springai.chat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import workshop.springai.services.ImageRecognitionService;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageRecognitionControllerTest {

    @Mock
    private ImageRecognitionService imageRecognitionService;

    @InjectMocks
    private ImageRecognitionController imageRecognitionController;

    @Test
    void shouldProcessImageSuccessfully() throws IOException {
        // Given
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test-image.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );
        String expectedResponse = "This is a test image";
        when(imageRecognitionService.processImage(file)).thenReturn(expectedResponse);

        // When
        ResponseEntity<String> response = imageRecognitionController.processImage(file);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .isNotNull()
            .isEqualTo(expectedResponse);
    }

    @Test
    void shouldHandleErrorWhenProcessingImage() throws IOException {
        // Given
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test-image.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );
        String errorMessage = "Processing failed";
        when(imageRecognitionService.processImage(file)).thenThrow(new IOException(errorMessage));

        // When
        ResponseEntity<String> response = imageRecognitionController.processImage(file);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody())
            .isNotNull()
            .contains("Image processing: process failed")
            .contains(errorMessage);
    }
}