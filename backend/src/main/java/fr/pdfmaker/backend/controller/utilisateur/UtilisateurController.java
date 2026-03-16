package fr.pdfmaker.backend.controller.utilisateur;

import fr.pdfmaker.backend.model.dto.utilisateur.InscriptionRequestDto;
import fr.pdfmaker.backend.model.dto.utilisateur.LoginDto;
import fr.pdfmaker.backend.model.dto.utilisateur.UtilisateurDto;
import fr.pdfmaker.backend.service.utilisateur.IUtilisateurService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Long> createUtilisateur( @Valid @RequestBody InscriptionRequestDto user) {

        try{
           Long id =  userService.createUser(user);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        }catch (Exception e ){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    @PutMapping("/update/{idUser}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<Long> updateUtilisateur(@PathVariable Long idUser,  @RequestBody UtilisateurDto user) {
        try{
          userService.updateUser(user);
          return new ResponseEntity<>(idUser, HttpStatus.ACCEPTED);
        }catch (Exception e ){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    @PostMapping("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<UtilisateurDto> connexionUtilisateur( @RequestBody LoginDto user) {

        try{
            UtilisateurDto userDto =  userService.loginUser(user);
            return new ResponseEntity(userDto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
