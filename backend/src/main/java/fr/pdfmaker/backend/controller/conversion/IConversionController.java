package fr.pdfmaker.backend.controller.conversion;

import fr.pdfmaker.backend.model.dto.conversion.ConversionResultatDto;
import fr.pdfmaker.backend.model.dto.conversion.ImageConversionRequestDto;
import fr.pdfmaker.backend.model.dto.conversion.TxtConversionRequestDto;
import fr.pdfmaker.backend.model.entity.Utilisateur;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Interface pour le contrôleur de conversion. Cette interface définit les méthodes que le contrôleur de conversion doit implémenter pour gérer les requêtes liées à la conversion de fichiers en PDF.
 * Les méthodes peuvent inclure des opérations telles que la conversion de fichiers, la récupération de liste des conversions effectuées, la suppression de conversions, etc.
 * L'implémentation de cette interface sera réalisée dans une classe concrète, par exemple ConversionController, qui contiendra la logique métier pour traiter les requêtes de conversion.
 *
 */
public interface IConversionController {



    @PostMapping(value = "/txt-to-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ConversionResultatDto> convertirTxt(@ModelAttribute TxtConversionRequestDto fichier, @AuthenticationPrincipal Utilisateur user) throws Exception;

    @PostMapping(value = "/image-to-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ConversionResultatDto> convertirImage(@ModelAttribute ImageConversionRequestDto request, @AuthenticationPrincipal Utilisateur user) throws Exception;
}
