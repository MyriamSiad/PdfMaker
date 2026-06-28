package fr.pdfmaker.backend.mongo.document;

import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.cglib.core.Local;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;



@Getter  @Setter  @ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "AppError")
public class AppError {


    @Id
    private String id;
    private LocalDateTime timestamp;
    private String exceptionClass;
    private String message;
    private String stackTrace;
    private String uri;
    private String origin;
}
