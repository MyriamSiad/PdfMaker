package fr.pdfmaker.backend.controller;

import fr.pdfmaker.backend.model.dto.UtilisateurCreationDto;
import fr.pdfmaker.backend.model.entity.Utilisateur;
import fr.pdfmaker.backend.service.IUtilisateurService;
import jakarta.ws.rs.Produces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Consumes;


@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/rest/user")
public class UtilisateurController implements IUtilisateurController {

    @Autowired
    private IUtilisateurService userService;

    public String getInfos() {
        return "";
    }


    @Override
    @PostMapping("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<Long> createUtilisateur( @RequestBody UtilisateurCreationDto user) {

        try{
           Long id =  userService.addOrUpdateUser(user);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        }catch (Exception e ){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    public ResponseEntity<UtilisateurCreationDto> connexionUtilisateur(Utilisateur user) {
        return null;
    }
}
