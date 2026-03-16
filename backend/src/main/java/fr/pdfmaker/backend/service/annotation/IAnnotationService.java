package fr.pdfmaker.backend.service.annotation;

import fr.pdfmaker.backend.exception.FichierIntrouvableException;
import fr.pdfmaker.backend.exception.PdfManipulationException;
import fr.pdfmaker.backend.exception.UnsupportedFormatException;
import fr.pdfmaker.backend.model.dto.annotation.AnnotationDto;
import fr.pdfmaker.backend.model.dto.conversion.ConversionResultatDto;

public interface IAnnotationService {
    /**
     * Ajoute une annotation textuelle sur la page indiquée du PDF source
     * et retourne le PDF annoté sous forme de byte[].
     *
     * @param request DTO contenant le fichier source, le contenu de l'annotation,
     *                la page cible (base 1) et la position (x, y en points)
     * @return résultat contenant le PDF annoté en mémoire
     * @throws FichierIntrouvableException si le fichier source est absent
     * @throws UnsupportedFormatException  si le fichier source n'est pas un PDF valide
     * @throws PdfManipulationException    si l'annotation échoue (page hors limites, PDF protégé…)
     */
    ConversionResultatDto annoter(AnnotationDto request);

    /**
     * Vérifie que les magic bytes correspondent à un PDF.
     *
     * @param magicBytes premiers octets du fichier
     * @throws UnsupportedFormatException si le fichier n'est pas un PDF
     */
    void verifierFormat(byte[] magicBytes);
}
