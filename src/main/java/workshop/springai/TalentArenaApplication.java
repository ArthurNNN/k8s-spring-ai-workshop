package workshop.springai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import workshop.springai.config.TalentArenaProperties;

import java.net.InetAddress;

@Slf4j
@EnableConfigurationProperties({TalentArenaProperties.class})
@SpringBootApplication
public class TalentArenaApplication implements CommandLineRunner {

	private final Environment env;

    public TalentArenaApplication(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
		SpringApplication.run(TalentArenaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String protocol = env.getProperty("server.ssl.key-store") != null ? "https" : "http";
		String serverPort = env.getProperty("server.port", "8080");
		String contextPath = env.getProperty("server.servlet.context-path", "");
		String hostAddress = InetAddress.getLocalHost().getHostAddress();
		String profiles = String.join(", ", env.getActiveProfiles());

		log.info("""
                
                ----------------------------------------------------------
                {} is running!
                Profile(s): {}
                URL:        {}://{}:{}{}
                ----------------------------------------------------------
                """,
				TalentArenaApplication.class.getSimpleName(),
				profiles.isEmpty() ? "default" : profiles,
				protocol,
				hostAddress,
				serverPort,
				contextPath
		);
	}
}
