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
