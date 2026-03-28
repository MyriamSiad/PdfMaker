package fr.pdfmaker.backend.controller.conversion;

import fr.pdfmaker.backend.model.dto.conversion.ConversionResultatDto;
import fr.pdfmaker.backend.model.dto.conversion.ImageConversionRequestDto;
import fr.pdfmaker.backend.model.dto.conversion.TxtConversionRequestDto;
import fr.pdfmaker.backend.service.conversion.IConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Override
    public ResponseEntity<byte[]> getFichier(String fullPath) {
        return null;
    }


    @Override
    @PostMapping(value = "/txt-to-pdf" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ConversionResultatDto> convertirTxt ( @ModelAttribute TxtConversionRequestDto fichier){

        if (fichier == null || fichier.getFichier().isEmpty() || fichier.getNomFichierSortie() == null || fichier.getNomFichierSortie().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        TxtConversionRequestDto request = new TxtConversionRequestDto();
        request.setFichier(fichier.getFichier());
        request.setNomFichierSortie(fichier.getNomFichierSortie());

        ConversionResultatDto resultat = txtToPdfService.convert(request);
        return ResponseEntity.ok(resultat);
    }

    @Override
    @PostMapping(value = "/image-to-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ConversionResultatDto> convertirImage  (@ModelAttribute ImageConversionRequestDto request) {

        if (request == null
                || request.getFichier() == null
                || request.getNomFichierSortie() == null) {
            return ResponseEntity.badRequest().build();
        }

        ConversionResultatDto resultat = imgToPdfService.convert(request);
        return ResponseEntity.ok(resultat);
    }


}
