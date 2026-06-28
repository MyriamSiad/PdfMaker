package fr.pdfmaker.backend.mongo.controller;


import fr.pdfmaker.backend.mongo.dto.ErrorLogDTO;
import fr.pdfmaker.backend.mongo.service.AppErrorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin (origins =  "http://localhost:4200")
@RequestMapping("/api/errors")
public class AppErrorController {


    private final AppErrorService errorLogService;

    public AppErrorController (AppErrorService errorLogService) {
        this.errorLogService = errorLogService;
    }

    // Seul endpoint nécessaire : écriture depuis le frontend
    @PostMapping
    public ResponseEntity<Void> logError(@RequestBody ErrorLogDTO dto) {
        errorLogService.logFromDto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



}
