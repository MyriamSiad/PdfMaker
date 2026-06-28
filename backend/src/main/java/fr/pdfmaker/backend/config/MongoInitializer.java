package fr.pdfmaker.backend.config;


import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration

public class MongoInitializer {
    private final MongoTemplate mongoTemplate;

    public MongoInitializer(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        //appeler autant de fois que nécessaire pour créer les collections (des documents)
    }

    @PostConstruct
    public void init() {
        // Initialisation de la base de données MongoDB
        // Par exemple, vous pouvez créer des collections ou insérer des documents de test ici
        if (!mongoTemplate.collectionExists("AppError")) {
            mongoTemplate.createCollection("AppError");
        }
    }

    private void createLoginHistoriqueCollection(Class<?> documentClass) {
        if (!mongoTemplate.collectionExists("AppError")) {
            mongoTemplate.createCollection(documentClass);
        }
    }
}
