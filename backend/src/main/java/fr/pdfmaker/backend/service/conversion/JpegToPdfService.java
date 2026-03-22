package fr.pdfmaker.backend.service.conversion;

import org.openpdf.text.Document;
import org.openpdf.text.Image;
import org.openpdf.text.PageSize;
import org.openpdf.text.Rectangle;
import org.openpdf.text.pdf.PdfWriter;

import fr.pdfmaker.backend.exception.ConversionException;
import fr.pdfmaker.backend.exception.FichierIntrouvableException;
import fr.pdfmaker.backend.exception.UnsupportedFormatException;
import fr.pdfmaker.backend.model.dto.conversion.ConversionResultatDto;
import fr.pdfmaker.backend.model.dto.conversion.ImageConversionRequestDto;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Convertit un fichier JPEG en PDF via OpenPDF.
 *
 * Magic bytes JPEG : FF D8 FF (3 premiers octets).
 *
 * Si adapterALaPage = true  → l'image est redimensionnée pour tenir dans la page A4
 *                              en conservant le ratio hauteur/largeur.
 * Si adapterALaPage = false → la page est dimensionnée aux dimensions de l'image.
 */
@Service
public class JpegToPdfService implements IConversionService<ImageConversionRequestDto> {

    private static final byte[] MAGIC_JPEG = {
            (byte) 0xFF, (byte) 0xD8, (byte) 0xFF
    };


    @Override
    public ConversionResultatDto convertToPdf(String inputPathFichier) {
        return null;
    }

    @Override
    public ConversionResultatDto convert(ImageConversionRequestDto request) {

        Path cheminFichier = request.getCheminFichier();

        if (!Files.exists(cheminFichier)) {
            throw new FichierIntrouvableException(cheminFichier);
        }

        try {
            byte[] magicBytes = lireMagicBytes(cheminFichier);
            verifierFormat(magicBytes);

            byte[] donneesImage = Files.readAllBytes(cheminFichier);
            byte[] contenuPdf   = genererPdf(donneesImage, request.isAdapterALaPage());

            return new ConversionResultatDto(request.getNomFichierSortie() + ".pdf", contenuPdf);

        } catch (UnsupportedFormatException | FichierIntrouvableException e) {
            throw e;
        } catch (IOException e) {
            throw new ConversionException(
                    "Erreur lors de la lecture du fichier JPEG : " + cheminFichier.getFileName(), e);
        } catch (Exception e) {
            throw new ConversionException(
                    "Erreur lors de la conversion JPEG → PDF : " + cheminFichier.getFileName(), e);
        }
    }

    @Override
    public void verifierFormat(byte[] magicBytes) {
        if (!commenceParSignature(magicBytes, MAGIC_JPEG)) {
            throw new UnsupportedFormatException("JPEG", "signature FF D8 FF non trouvée");
        }
    }

    // ---------------------------------------------------------------------- génération PDF

    private byte[] genererPdf(byte[] donneesImage, boolean adapterALaPage) {

        try (ByteArrayOutputStream sortie = new ByteArrayOutputStream()) {

            Image image = Image.getInstance(donneesImage);

            Rectangle taillePage;
            if (adapterALaPage) {
                taillePage = PageSize.A4;
                adapterImageALaPage(image, taillePage);
            } else {
                // La page prend les dimensions exactes de l'image (en points)
                taillePage = new Rectangle(image.getWidth(), image.getHeight());
                image.setAbsolutePosition(0, 0);
            }

            Document document = new Document(taillePage, 0, 0, 0, 0);
            PdfWriter.getInstance(document, sortie);
            document.open();
            document.add(image);
            document.close();

            return sortie.toByteArray();

        } catch (Exception e) {
            throw new ConversionException("Erreur OpenPDF lors de la génération du PDF depuis JPEG", e);
        }
    }

    /**
     * Redimensionne l'image pour qu'elle tienne dans la page
     * en conservant le ratio hauteur/largeur.
     * L'image est centrée sur la page.
     */
    private void adapterImageALaPage(Image image, Rectangle page) {
        float largeurPage  = page.getWidth();
        float hauteurPage  = page.getHeight();
        float largeurImage = image.getWidth();
        float hauteurImage = image.getHeight();

        float ratioLargeur = largeurPage  / largeurImage;
        float ratioHauteur = hauteurPage  / hauteurImage;
        float ratio        = Math.min(ratioLargeur, ratioHauteur);

        float nouvelleLargeur = largeurImage * ratio;
        float nouvelleHauteur = hauteurImage * ratio;

        image.scaleAbsolute(nouvelleLargeur, nouvelleHauteur);
        image.setAbsolutePosition(
                (largeurPage  - nouvelleLargeur) / 2f,
                (hauteurPage  - nouvelleHauteur) / 2f
        );
    }

    // ---------------------------------------------------------------------- utilitaires privés

    private byte[] lireMagicBytes(Path chemin) throws IOException {
        byte[] buffer = new byte[8];
        try (var stream = Files.newInputStream(chemin)) {
            int lu = stream.read(buffer);
            if (lu < buffer.length) {
                byte[] tronque = new byte[lu];
                System.arraycopy(buffer, 0, tronque, 0, lu);
                return tronque;
            }
        }
        return buffer;
    }

    private boolean commenceParSignature(byte[] magicBytes, byte[] signature) {
        if (magicBytes.length < signature.length) return false;
        for (int i = 0; i < signature.length; i++) {
            if (magicBytes[i] != signature[i]) return false;
        }
        return true;
    }
}
