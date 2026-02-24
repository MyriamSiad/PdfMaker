package fr.pdfmaker.backend.config;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableMongoRepositories(basePackages = "fr.pdfmaker.backend.mongo.repository")
@Configuration
public class MongoConfig {
}



