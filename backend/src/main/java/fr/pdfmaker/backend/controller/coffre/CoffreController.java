package fr.pdfmaker.backend.controller.coffre;


import fr.pdfmaker.backend.model.dto.coffrefort.PdfEncryptRequestDto;
import fr.pdfmaker.backend.model.dto.coffrefort.PdfEncryptResultDto;
import fr.pdfmaker.backend.model.dto.conversion.ConversionResultatDto;
import fr.pdfmaker.backend.model.dto.conversion.TxtConversionRequestDto;
import fr.pdfmaker.backend.model.dto.dossier.DossierRequestModification;
import fr.pdfmaker.backend.model.dto.dossier.DossierResponseDto;
import fr.pdfmaker.backend.model.dto.fichier.FichierDto;
import fr.pdfmaker.backend.model.dto.fichier.FichierModificationRequest;
import fr.pdfmaker.backend.model.dto.utilisateur.UtilisateurDto;
import fr.pdfmaker.backend.model.dto.utilisateur.UtilisateurSecretDetailDto;
import fr.pdfmaker.backend.model.entity.DossierVirtuel;
import fr.pdfmaker.backend.model.entity.Fichier;
import fr.pdfmaker.backend.model.entity.Utilisateur;
import fr.pdfmaker.backend.service.FichierService;
import fr.pdfmaker.backend.service.coffrefort.CoffreFortService;
import fr.pdfmaker.backend.service.coffrefort.CoffreFortSessionService;
import fr.pdfmaker.backend.service.coffrefort.EncryptService;
import fr.pdfmaker.backend.service.dossier.DossierVirtuelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@CrossOrigin (origins = "http://localhost:4200")
@RequestMapping("/api/rest/coffre-fort")
public class CoffreController {


    @Autowired
    private  EncryptService  encryptCoffreService;

    @Autowired
    private CoffreFortSessionService vaultSessionService;

    @Autowired
    private DossierVirtuelService dossierVirtuelService;

    @Autowired
    private FichierService fichierService;

    @PostMapping("/open")
    public ResponseEntity<Map<String, String>> openVault(@AuthenticationPrincipal Utilisateur utilisateur,  @RequestBody Map<String, String> body) throws Exception {

        String password = body.get("password");
        if (password == null || password.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Mot de passe requis"));
        }
        Long idUser = utilisateur.getIdUser();
        vaultSessionService.openVault(idUser, password);
        return ResponseEntity.ok(Map.of("status", "Coffre ouvert"));
    }


    @PostMapping("/close")
    public ResponseEntity<Map<String, String>> closeVault(@AuthenticationPrincipal Utilisateur utilisateur) throws Exception {
        Long idUser = utilisateur.getIdUser();
        vaultSessionService.closeVault(idUser);
        return ResponseEntity.ok(Map.of("status", "Coffre verrouillé"));
    }

    @PostMapping("/encrypt")
    public ResponseEntity<byte[]> encryptFile(@AuthenticationPrincipal Utilisateur utilisateur ,  @RequestParam("file") MultipartFile file , @RequestParam("idDossier") Long idDossier) throws Exception {
        try {
            Long idUser = utilisateur.getIdUser();
            byte[] encrypted = encryptCoffreService.encryptPdf(idUser, file.getBytes(), file.getOriginalFilename() , idDossier);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=encrypted.bin")
                    .body(encrypted);
        }catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }

    }

    @PostMapping("/encrypt-file-test")
    public ResponseEntity<Map<String, String>> testEndpoint() {
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @GetMapping("/dossiers")
    public ResponseEntity <Set<DossierResponseDto>> getDossiers(@AuthenticationPrincipal Utilisateur utilisateur) throws Exception {

        try {
            return ResponseEntity.ok(dossierVirtuelService.getAllDossier(utilisateur.getIdUser()));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("=== ERREUR === " + e.getMessage());
            return ResponseEntity.status(500).build();
        }


    }

    @GetMapping("/dossiers/{idDossier}/fichiers")
    public ResponseEntity<List<FichierDto>> getFichiers(
            @PathVariable Long idDossier) {
        return ResponseEntity.ok(fichierService.getFichiersByIdDossier(idDossier));
    }

    @GetMapping("/secrets")
    public ResponseEntity<UtilisateurSecretDetailDto> getSecrets( @AuthenticationPrincipal Utilisateur authenticatedPrincipal) throws Exception {

        try {
            return ResponseEntity.ok(encryptCoffreService.userSecretDetail(authenticatedPrincipal.getIdUser()));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("=== ERREUR === " + e.getMessage());
            return ResponseEntity.status(500).build();
        }


    }
    @DeleteMapping("delete-dossier/{idDossier}")

    public ResponseEntity<Void> deleteDossier(@PathVariable Long idDossier , @AuthenticationPrincipal Utilisateur authenticatedPrincipal) throws Exception {
        try{
            dossierVirtuelService.supprimerDossier(idDossier);
            return ResponseEntity.noContent().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("modify-dossier/{idDossier}")

    public ResponseEntity<DossierResponseDto> modifyDossier(@PathVariable Long idDossier , @RequestBody DossierRequestModification request, @AuthenticationPrincipal Utilisateur authenticatedPrincipal) throws Exception {
        try{

            return ResponseEntity.ok(dossierVirtuelService.modifierNomDossier(request.getNomDossier(), idDossier));
        }catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping ("delete-file/{idFile}")

    public ResponseEntity<Void>deleteFile(@PathVariable Long idFile, @AuthenticationPrincipal Utilisateur authenticatedPrincipal) throws Exception {
        fichierService.deleteFichierById(idFile);
        return ResponseEntity.noContent().build();

    }

    @PutMapping("update-filename/{idFile}")
    public ResponseEntity<Void> modifyFile(@PathVariable Long idFile, @AuthenticationPrincipal Utilisateur user, @RequestBody FichierModificationRequest request) throws Exception {
        fichierService.updateFichier(idFile, request.getNomFichier());

        return ResponseEntity.noContent().build();

    }



    @PostMapping("add-dossier")
    public ResponseEntity<DossierResponseDto> addDossier( @RequestBody DossierRequestModification request, @AuthenticationPrincipal Utilisateur authenticatedPrincipal) throws Exception {
        try{

            return ResponseEntity.ok(dossierVirtuelService.creerDossier(request.getNomDossier(),authenticatedPrincipal.getIdUser()));
        }catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping("/decrypt/{idFichier}")
    public ResponseEntity<byte[]> decryptFile( @AuthenticationPrincipal Utilisateur utilisateur,   @PathVariable Long idFichier) throws Exception {


        try {
            Fichier file = fichierService.getFichierById(idFichier);
            Path path = Paths.get(file.getCheminLocal());
            byte[] fileBytes = Files.readAllBytes(path);
            byte[] decrypted = encryptCoffreService.decryptPdf(utilisateur.getIdUser(),fileBytes);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=decrypted.pdf")
                    .body(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("=== ERREUR === " + e.getMessage());
            return ResponseEntity.status(500).build();
        }

    }

}
