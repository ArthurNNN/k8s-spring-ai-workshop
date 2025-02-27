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