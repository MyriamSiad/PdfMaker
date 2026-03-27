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
import org.springframework.web.multipart.MultipartFile;

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
public class ImageToPdfService implements IConversionService<ImageConversionRequestDto> {


    private static final byte[] MAGIC_JPEG = { (byte)0xFF, (byte)0xD8, (byte)0xFF };
    private static final byte[] MAGIC_PNG  = { (byte)0x89, 0x50, 0x4E, 0x47 };


    @Override
    public ConversionResultatDto convertToPdf(String inputPathFichier) {
        return null;
    }

    @Override
    public ConversionResultatDto convert(ImageConversionRequestDto request) {
        MultipartFile fichier = request.getFichier();

        if (fichier == null || fichier.isEmpty()) {

            throw new FichierIntrouvableException("Aucun fichier reçu");
        }

        try {
            byte[] magicBytes = lireMagicBytes(fichier.getBytes());
            verifierFormat(magicBytes);

            byte[] donneesImage = fichier.getBytes();
            byte[] contenuPdf   = genererPdf(donneesImage, request.isAdapterALaPage());

            return new ConversionResultatDto(request.getNomFichierSortie() + ".pdf", contenuPdf);

        } catch (UnsupportedFormatException | FichierIntrouvableException e) {
            throw e;
        } catch (IOException e) {
            throw new ConversionException(
                    "Erreur lors de la lecture du fichier  : " + fichier.getName(), e);
        } catch (Exception e) {
            throw new ConversionException(
                    "Erreur lors de la conversion Image  → PDF : " + fichier.getName(), e);
        }
    }

    @Override
    public void verifierFormat(byte[] magicBytes) {
        boolean estJpeg = commenceParSignature(magicBytes, MAGIC_JPEG);
        boolean estPng  = commenceParSignature(magicBytes, MAGIC_PNG);

        if (!estJpeg && !estPng) {
            throw new UnsupportedFormatException(
                    "IMAGE", "le fichier n'est ni un JPEG ni un PNG");
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
            throw new ConversionException("Erreur OpenPDF lors de la génération du PDF depuis une image", e);
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

    /**
     * Lit les 8 premiers octets du fichier pour en extraire les magic bytes.
     * Si le fichier est plus petit que 8 octets, retourne null.
     * */
    private byte[] lireMagicBytes(byte[] bytes) throws IOException {
        byte[] buffer = new byte[8];

        try{
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = (byte) bytes[i];
            }

            return buffer;
        }
        catch (IndexOutOfBoundsException e) {

            return null;
        }
    }

    private boolean commenceParSignature(byte[] magicBytes, byte[] signature) {
        if (magicBytes.length < signature.length) return false;
        for (int i = 0; i < signature.length; i++) {
            if (magicBytes[i] != signature[i]) return false;
        }
        return true;
    }
}
