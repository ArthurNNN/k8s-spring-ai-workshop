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
