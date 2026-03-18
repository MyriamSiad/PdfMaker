package fr.pdfmaker.backend.controller.conversion;

import fr.pdfmaker.backend.model.dto.conversion.ConversionResultatDto;
import fr.pdfmaker.backend.model.dto.conversion.TxtConverstionRequestDto;
import fr.pdfmaker.backend.service.conversion.IConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/rest/pdf/conversion/txt")
public class ConversionController implements IConversionController {


    @Qualifier("txtToPdfService")
    @Autowired
    private IConversionService conversionService;

    @Override
    public ResponseEntity<byte[]> getFichier(String fullPath) {
        return null;
    }


    @Override
    public ResponseEntity<ConversionResultatDto> convertirFichier(TxtConverstionRequestDto request) {
        if (request == null  || request.getCheminFichier() == null || request.getNomFichierSortie() == null) {
            return ResponseEntity.badRequest().build();
        }
        ConversionResultatDto resultat = conversionService.convert(request);
         return ResponseEntity.ok(resultat);

    }
}
