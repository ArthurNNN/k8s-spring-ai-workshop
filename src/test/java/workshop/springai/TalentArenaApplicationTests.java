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
