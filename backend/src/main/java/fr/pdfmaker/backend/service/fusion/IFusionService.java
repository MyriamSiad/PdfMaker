package fr.pdfmaker.backend.service.fusion;


import fr.pdfmaker.backend.exception.FichierIntrouvableException;
import fr.pdfmaker.backend.exception.PdfManipulationException;
import fr.pdfmaker.backend.exception.UnsupportedFormatException;
import fr.pdfmaker.backend.model.dto.conversion.ConversionResultatDto;
import fr.pdfmaker.backend.model.dto.fusion.FusionRequestDto;
/**
 * Contrat pour la fusion de plusieurs PDFs en un seul document.
 * L'ordre des fichiers dans FusionRequestDto.cheminsFichiers
 * détermine l'ordre des pages dans le PDF produit.
 */
public interface IFusionService {

    /**
     * Fusionne les PDFs listés dans la requête en un seul PDF.
     * Retourne un ConversionResultatDto portant le byte[] du PDF produit.
     *
     * @param request DTO contenant la liste ordonnée des PDFs source, jamais null
     * @return résultat contenant le PDF fusionné en mémoire
     * @throws FichierIntrouvableException si un des fichiers source est absent
     * @throws UnsupportedFormatException  si un fichier n'est pas un PDF valide (magic bytes)
     * @throws PdfManipulationException    si la fusion échoue
     */
    ConversionResultatDto fusionner(FusionRequestDto request);

    /**
     * Vérifie que les magic bytes correspondent à un PDF.
     * Appelé pour chaque fichier source avant la fusion.
     *
     * @param magicBytes premiers octets du fichier
     * @throws UnsupportedFormatException si le fichier n'est pas un PDF
     */
    void verifierFormat(byte[] magicBytes);

}