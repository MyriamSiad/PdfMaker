package fr.pdfmaker.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JacksonConfigLocalDate {

    private static final String DATE_FORMAT = "dd-MM-yyyy";

    @Bean
    public ObjectMapper objectMapper() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(
                LocalDate.class,
                new LocalDateDeserializer(dateFormatter)
        );
        javaTimeModule.addSerializer(
                LocalDate.class,
                new LocalDateSerializer(dateFormatter)
        );

        return JsonMapper.builder()
                .addModule(javaTimeModule)
                .build();
    }
}
