package workshop.springai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("talent-arena")
public record TalentArenaProperties(Map<String, Website> websites) {
}