package fr.pdfmaker.backend.mongo.service;


import fr.pdfmaker.backend.mongo.document.AppError;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ErrorLogFileService {

    private static final String LOG_DIR = "logs/errors/";
    private static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter LOG_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Appelé à chaque sauvegarde d'erreur
    public void writeToLogFile(AppError errorLog) {
        try {
            Path logDir = Paths.get(LOG_DIR);
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }

            String fileName = "errors-" + LocalDate.now().format(FILE_FORMATTER) + ".log";
            Path logFile = logDir.resolve(fileName);

            String logEntry = buildLogEntry(errorLog);

            Files.writeString(logFile, logEntry,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

        } catch (IOException e) {
            System.err.println("Impossible d'écrire dans le fichier log : " + e.getMessage());
        }
    }

    private String buildLogEntry(AppError log) {
        return """
            ============================================================
            [%s] %s | %s
            URI       : %s
            EXCEPTION : %s
            MESSAGE   : %s
            STACKTRACE:
            %s
            """.formatted(
                log.getTimestamp().format(LOG_FORMATTER),
                log.getOrigin(),
                log.getId(),
                log.getUri(),
                log.getExceptionClass(),
                log.getMessage(),
                log.getStackTrace()
        );
    }
}
