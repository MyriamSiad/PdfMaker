package fr.pdfmaker.backend.mongo.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

//Pour respecter la convention de l'API REST, on utilise un DTO pour exposer les données de LoginHistorique
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class ErrorLogDTO {
    private String exceptionClass;
    private String message;
    private String stackTrace;
    private String uri;
    private String origin;
}
