package workshop.springai;

import org.springframework.boot.SpringApplication;
import org.testcontainers.utility.TestcontainersConfiguration;

public class TestTalentArenaApplication {

    public static void main(String[] args) {
        SpringApplication.from(TalentArenaApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }

}
