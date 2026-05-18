package fr.pdfmaker.backend.controller.coffre;


import fr.pdfmaker.backend.model.dto.coffrefort.PdfEncryptRequestDto;
import fr.pdfmaker.backend.model.dto.coffrefort.PdfEncryptResultDto;
import fr.pdfmaker.backend.model.dto.conversion.ConversionResultatDto;
import fr.pdfmaker.backend.model.dto.conversion.TxtConversionRequestDto;
import fr.pdfmaker.backend.model.dto.utilisateur.UtilisateurDto;
import fr.pdfmaker.backend.model.dto.utilisateur.UtilisateurSecretDetailDto;
import fr.pdfmaker.backend.model.entity.Utilisateur;
import fr.pdfmaker.backend.service.coffrefort.CoffreFortService;
import fr.pdfmaker.backend.service.coffrefort.EncryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin (origins = "http://localhost:4200")
@RequestMapping("/api/rest/coffre-fort")
public class CoffreController {


    @Autowired
    private  EncryptService  encryptCoffreService;



    @PostMapping("/encrypt-file-test")
    public ResponseEntity<Map<String, String>> testEndpoint() {
        return ResponseEntity.ok(Map.of("status", "ok"));
    }


    @GetMapping("/secrets/{idUser}")
    public ResponseEntity<UtilisateurSecretDetailDto> getSecrets(@PathVariable Long idUser) throws Exception {

        try {
            return ResponseEntity.ok(encryptCoffreService.userSecretDetail(idUser));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("=== ERREUR === " + e.getMessage());
            return ResponseEntity.status(500).build();
        }


    }
    @PostMapping(value = "/encrypt-file" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte []> encryptFile (@ModelAttribute PdfEncryptRequestDto fichier , @AuthenticationPrincipal Utilisateur utilisateur) {

        if (fichier == null || fichier.getFichierPdf()  == null || fichier.getFichierPdf().isEmpty()|| fichier.getFichierPdf().getName().isBlank()) {

            return ResponseEntity.badRequest().build();
        }


        try {
            PdfEncryptResultDto resultat = new PdfEncryptResultDto();

            byte[] fileEncryptedBytes = encryptCoffreService.encryptPdf(utilisateur.getIdUser(), fichier.getFichierPdf().getBytes() , fichier.getMotDePasse());

            resultat.setFichierPdf(fileEncryptedBytes);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=fichier.enc")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resultat.getFichierPdf());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("=== ERREUR === " + e.getMessage());
            return ResponseEntity.status(500).build();
        }

    }

}
