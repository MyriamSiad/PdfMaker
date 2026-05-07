package fr.pdfmaker.backend.controller.coffre;


import fr.pdfmaker.backend.model.dto.coffrefort.PdfEncryptRequestDto;
import fr.pdfmaker.backend.model.dto.coffrefort.PdfEncryptResultDto;
import fr.pdfmaker.backend.model.dto.conversion.ConversionResultatDto;
import fr.pdfmaker.backend.model.dto.conversion.TxtConversionRequestDto;
import fr.pdfmaker.backend.model.dto.utilisateur.UtilisateurDto;
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

@RestController
@CrossOrigin (origins = "http://localhost:4200")
@RequestMapping("/api/rest/coffre-fort")
public class CoffreController {


    private EncryptService  encryptCoffreService;






    @PostMapping(value = "/encrypt-file" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PdfEncryptResultDto> encryptFile (@ModelAttribute PdfEncryptRequestDto fichier , @AuthenticationPrincipal Utilisateur utilisateur) {


        if (fichier == null || fichier.getFichierPdf().isEmpty()|| fichier.getFichierPdf().getName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }


        try {
            PdfEncryptResultDto resultat = new PdfEncryptResultDto();

            byte[] fileEncryptedBytes = encryptCoffreService.encryptPdf(utilisateur.getIdUser(), fichier.getFichierPdf().getBytes());

                resultat.setFichierPdf(fileEncryptedBytes);
            return ResponseEntity.ok(resultat);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }



    }

}
