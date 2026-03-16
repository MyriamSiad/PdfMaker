package fr.pdfmaker.backend.service.separation;

import fr.pdfmaker.backend.exception.FichierIntrouvableException;
import fr.pdfmaker.backend.exception.PdfManipulationException;
import fr.pdfmaker.backend.exception.UnsupportedFormatException;
import fr.pdfmaker.backend.model.dto.conversion.ConversionResultatDto;
import fr.pdfmaker.backend.model.dto.separation.SeparationRequestDto;

public interface ISeparationService {

     /**
      * Sépare un PDF en plusieurs fichiers selon les pages spécifiées.
      * Retourne un tableau de byte[] pour chaque PDF produit.
      *
      * @param request DTO contenant le chemin du PDF source et les pages à extraire, jamais null
      * @return tableau de byte[] pour chaque PDF extrait
      * @throws FichierIntrouvableException si le fichier source est absent
      * @throws UnsupportedFormatException  si le fichier n'est pas un PDF valide (magic bytes)
      * @throws PdfManipulationException    si la séparation échoue
      */
     ConversionResultatDto separer(SeparationRequestDto request);


    /**
     * Vérifie que les magic bytes correspondent à un PDF.
     *
     * @param magicBytes premiers octets du fichier
     * @throws UnsupportedFormatException si le fichier n'est pas un PDF
     */
    void verifierFormat(byte[] magicBytes);

}
