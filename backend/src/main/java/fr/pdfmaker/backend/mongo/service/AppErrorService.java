package fr.pdfmaker.backend.mongo.service;

import fr.pdfmaker.backend.mongo.document.AppError;
import fr.pdfmaker.backend.mongo.dto.ErrorLogDTO;
import fr.pdfmaker.backend.mongo.repository.AppErrorRepository;
import org.springframework.stereotype.Service;

@Service

public class AppErrorService {

    private final AppErrorRepository repository;
    private final ErrorLogFileService errorLogFileService;
    public AppErrorService(AppErrorRepository repository, ErrorLogFileService errorLogFileService) {

        this.repository = repository;
        this.errorLogFileService = errorLogFileService;
    }

    public void logError(String exceptionClass, String message,
                         String stackTrace, String uri, String origin) {
        AppError log = new  AppError(exceptionClass, message, stackTrace, uri, origin);
        AppError saved = repository.save(log);
        errorLogFileService.writeToLogFile(saved);
    }

    public void logFromDto(ErrorLogDTO dto) {
        AppError log = new  AppError (
                dto.getExceptionClass(),
                dto.getMessage(),
                dto.getStackTrace(),
                dto.getUri(),
                dto.getOrigin()
        );

        AppError saved = repository.save(log);
        errorLogFileService.writeToLogFile(saved);

    }
}