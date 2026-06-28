package fr.pdfmaker.backend.exception;

import fr.pdfmaker.backend.mongo.service.AppErrorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Gestionnaire centralisé des exceptions.
 * Intercepte toutes les exceptions métier et retourne
 * une réponse JSON structurée au frontend JavaFX.
 */
@RestControllerAdvice
public class GlobalHandlerException {

    @Autowired
    private AppErrorService appErrorService;

    @Autowired
    private HttpServletRequest request;

    @ExceptionHandler(FichierIntrouvableException.class)
    public ResponseEntity<Map<String, Object>> handleFichierIntrouvable(
            FichierIntrouvableException ex) {
        logBackendError(ex);
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }


    @ExceptionHandler(UnsupportedFormatException.class)
    public ResponseEntity<Map<String, Object>> handleUnsupportedFormat(
            UnsupportedFormatException ex) {
        logBackendError(ex);
        return buildResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage());
    }


    @ExceptionHandler(ConversionException.class)
    public ResponseEntity<Map<String, Object>> handleConversion(
            ConversionException ex) {
        logBackendError(ex);
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }


    @ExceptionHandler(PdfManipulationException.class)
    public ResponseEntity<Map<String, Object>> handlePdfManipulation(
            PdfManipulationException ex) {
        logBackendError(ex);
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenerique(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Erreur interne inattendue : " + ex.getMessage());
    }


    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status, String message) {

        Map<String, Object> corps = new LinkedHashMap<>();
        corps.put("timestamp", LocalDateTime.now().toString());
        corps.put("status", status.value());
        corps.put("erreur", status.getReasonPhrase());
        corps.put("message", message);

        return ResponseEntity.status(status).body(corps);
    }

    @ExceptionHandler(EmailDejaUtiliserException.class)
    public ResponseEntity<Map<String, String>> gererEmailDejaUtilise(EmailDejaUtiliserException e) {
        Map<String, String> erreur = new HashMap<>();
        erreur.put("message", e.getMessage());


        return ResponseEntity.status(409).body(erreur); // 409 = Conflict
    }

    private void logBackendError(Exception ex) {
        String stackTrace = Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));

       appErrorService.logError(
                ex.getClass().getName(),
                ex.getMessage(),
                stackTrace,
                request.getRequestURI(),
                "BACKEND"
        );
    }

    @ExceptionHandler (LoginIncorrectException.class)
    public ResponseEntity<Map<String, String>> gererLoginIncorrect(LoginIncorrectException e) {
        Map<String, String> erreur = new HashMap<>();
        erreur.put("message", e.getMessage());
        return ResponseEntity.status(401).body(erreur); // 401 = Unauthorized
    }
}
