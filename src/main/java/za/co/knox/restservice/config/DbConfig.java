package za.co.knox.restservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "za.co.knox.restservice.repository")
public class DbConfig {
}