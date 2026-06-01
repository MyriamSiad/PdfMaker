package fr.pdfmaker.backend.controller.conversion;

import fr.pdfmaker.backend.enums.LibelleOperationEnum;
import fr.pdfmaker.backend.model.dto.conversion.ConversionResultatDto;
import fr.pdfmaker.backend.model.dto.conversion.ImageConversionRequestDto;
import fr.pdfmaker.backend.model.dto.conversion.TxtConversionRequestDto;
import fr.pdfmaker.backend.model.entity.Utilisateur;
import fr.pdfmaker.backend.service.FichierService;
import fr.pdfmaker.backend.service.conversion.IConversionService;
import fr.pdfmaker.backend.service.utilisateur.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static fr.pdfmaker.backend.utils.DtoUserConverter.convertUserDtoToUser;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/rest/pdf/conversion")
public class ConversionController implements IConversionController {



    @Qualifier("txtToPdfService")
    @Autowired
    private IConversionService<TxtConversionRequestDto> txtToPdfService;

    @Qualifier("imageToPdfService")
    @Autowired
    private IConversionService<ImageConversionRequestDto> imgToPdfService;


    @Autowired
    private FichierService fichierService;


    @Autowired
    private UtilisateurService utilisateurService;


    @PostMapping(value = "/txt-to-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<ConversionResultatDto> convertirTxt(@ModelAttribute TxtConversionRequestDto fichier, @AuthenticationPrincipal Utilisateur user) throws Exception {

        if (fichier == null || fichier.getFichier().isEmpty() || fichier.getNomFichierSortie() == null || fichier.getNomFichierSortie().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            TxtConversionRequestDto request = new TxtConversionRequestDto();
            request.setFichier(fichier.getFichier());
            request.setNomFichierSortie(fichier.getNomFichierSortie());
            ConversionResultatDto resultat = txtToPdfService.convert(request);

           return ResponseEntity.ok(resultat);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping(value = "/image-to-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<ConversionResultatDto> convertirImage(@ModelAttribute ImageConversionRequestDto request, @AuthenticationPrincipal Utilisateur user) throws Exception {

        if (request == null
                || request.getFichier() == null
                || request.getNomFichierSortie() == null) {
            return ResponseEntity.badRequest().build();
        }

        try {

            ConversionResultatDto resultat = imgToPdfService.convert(request);

            return ResponseEntity.ok(resultat);
        }catch (Exception e) {
            ConversionResultatDto resultat = imgToPdfService.convert(request);

            throw new RuntimeException(e);
        }

    }


}
