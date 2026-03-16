package fr.pdfmaker.backend.service.conversion;

import fr.pdfmaker.backend.exception.ConversionException;
import fr.pdfmaker.backend.exception.FichierIntrouvableException;
import fr.pdfmaker.backend.exception.UnsupportedFormatException;
import fr.pdfmaker.backend.model.dto.conversion.ConversionRequestDto;
import fr.pdfmaker.backend.model.dto.conversion.ConversionResultatDto;

public interface IConversionService <T extends ConversionRequestDto> {

    /**
     * Convertit le fichier source décrit dans la requête en PDF.
     * Retourne un ConversionResultatDto portant le byte[] du PDF produit.
     *
     * @param request DTO de la requête, jamais null
     * @return résultat contenant le PDF en mémoire
     * @throws FichierIntrouvableException  si le fichier source est absent
     * @throws UnsupportedFormatException   si les magic bytes ne correspondent pas au format attendu
     * @throws ConversionException          si la conversion échoue
     */
    ConversionResultatDto convert(T request);

    /**
     * Vérifie que les magic bytes correspondent au format attendu par ce service.
     * Appelé en entrée de convert() — chaque service valide ce qu'il consomme.
     *
     * @param magicBytes premiers octets du fichier (4 à 8 octets suffisent)
     * @throws UnsupportedFormatException si le format ne correspond pas
     */
    void verifierFormat(byte[] magicBytes);
}
