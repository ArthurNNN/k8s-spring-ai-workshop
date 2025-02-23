package workshop.springai.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;



@Slf4j
@Service
public class IngestionService implements CommandLineRunner {

    private final VectorStore vectorStore;
    private final TextSplitter textSplitter;
    private final ResourcePatternResolver resourcePatternResolver;
    private final String resourcesLocation;

    public IngestionService(VectorStore vectorStore, TextSplitter textSplitter,
                            ResourcePatternResolver resourcePatternResolver,
                            @Value("classpath:/documents/*.pdf") String resourcesLocation) {
        this.vectorStore = vectorStore;
        this.textSplitter = textSplitter;
        this.resourcePatternResolver = resourcePatternResolver;
        this.resourcesLocation = resourcesLocation;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Ingesting data from resources: {}", resourcesLocation);

        Resource[] resources = resourcePatternResolver.getResources(resourcesLocation);

        for (Resource resource : resources) {
            log.info("Reading file: {}", resource.getFilename());
            TikaDocumentReader textReader = new TikaDocumentReader(resource);
            var documents = textSplitter.apply(textReader.get());
            vectorStore.accept(documents);
        }

        log.info("VectorStore Loaded with data!");
    }
}
