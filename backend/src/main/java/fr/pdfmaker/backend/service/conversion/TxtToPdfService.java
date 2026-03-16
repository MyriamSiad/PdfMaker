package fr.pdfmaker.backend.service.conversion;

import fr.pdfmaker.backend.exception.ConversionException;
import fr.pdfmaker.backend.exception.FichierIntrouvableException;
import fr.pdfmaker.backend.exception.UnsupportedFormatException;
import fr.pdfmaker.backend.model.dto.conversion.ConversionResultatDto;
import fr.pdfmaker.backend.model.dto.conversion.TxtConverstionRequestDto;
import fr.pdfmaker.backend.model.pdf.TxtConversionModel;
import fr.pdfmaker.backend.service.commun.SaveFileService;
import org.openpdf.text.*;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class TxtToPdfService  implements IConversionService <TxtConverstionRequestDto> {

    private static final byte[] MAGIC_PDF  = { 0x25, 0x50, 0x44, 0x46 };          // %PDF
    private static final byte[] MAGIC_JPEG = { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF }; // ÿØÿ
    private static final byte[] MAGIC_PNG  = { (byte) 0x89, 0x50, 0x4E, 0x47 };   // .PNG
    private static final byte[] MAGIC_GIF  = { 0x47, 0x49, 0x46, 0x38 };          // GIF8
    private static final byte[] MAGIC_ZIP  = { 0x50, 0x4B, 0x03, 0x04 };          // PK..
    private static final byte[] MAGIC_BMP  = { 0x42, 0x4D };                      // BM

    private static final List<byte[]> FORMATS_BINAIRES_CONNUS = List.of(
            MAGIC_PDF, MAGIC_JPEG, MAGIC_PNG, MAGIC_GIF, MAGIC_ZIP, MAGIC_BMP
    );

    @Override
    public ConversionResultatDto convert(TxtConverstionRequestDto request) {
        Path cheminFichier = request.getCheminFichier();

        if(!Files.exists(cheminFichier)){
            throw new FichierIntrouvableException("Le fichier source est introuvable : " + cheminFichier);

        }

        try {
            byte[] magicBytes = lireMagicBytes(cheminFichier);
            verifierFormat(magicBytes);

            Charset charset = resoudreCharset(request.getCharset());
            List<String> lignes = Files.readAllLines(cheminFichier, charset);

            TxtConversionModel modele = new TxtConversionModel();

            byte[] contenuPdf = genererPdf(lignes, modele);

            return new ConversionResultatDto(request.getNomFichierSortie() + ".pdf", contenuPdf);
        }catch (UnsupportedFormatException | FichierIntrouvableException e) {
            throw e;
        } catch (IOException e) {
            throw new ConversionException(
                    "Erreur lors de la lecture du fichier TXT : " + cheminFichier.getFileName(), e);
        } catch (Exception e) {
            throw new ConversionException(
                    "Erreur lors de la conversion TXT → PDF : " + cheminFichier.getFileName(), e);
        }

    }

    @Override
    public void verifierFormat(byte[] magicBytes) {
        for (byte[] signature : FORMATS_BINAIRES_CONNUS) {
            if (commenceParSignature(magicBytes, signature)) {
                throw new UnsupportedFormatException(
                        "TXT", "format binaire détecté (signature inconnue d'un fichier texte)");
            }
        }
    }


    private byte[] genererPdf(List<String> lignes, TxtConversionModel modele) {

        try (ByteArrayOutputStream sortie = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4,
                    modele.getMarginLeft(), modele.getMarginRight(),
                    modele.getMarginTop(), modele.getMarginBottom());

            PdfWriter.getInstance(document, sortie);
            document.open();

            Font police = FontFactory.getFont(
                    modele.getFont(),
                    modele.getFontSize()
            );

            for (String ligne : lignes) {
                // Les lignes vides produisent un paragraphe vide — conserve la mise en page
                Paragraph paragraphe = new Paragraph(
                        ligne.isEmpty() ? " " : ligne,
                        police
                );
                paragraphe.setSpacingAfter(0f);
                document.add(paragraphe);
            }

            document.close();
            return sortie.toByteArray();

        } catch (Exception e) {
            throw new ConversionException("Erreur OpenPDF lors de la génération du PDF", e);
        }
    }


    private  byte[] lireMagicBytes(Path chemin) throws IOException {
        int maxLen = FORMATS_BINAIRES_CONNUS.stream()
                .mapToInt(s -> s.length)
                .max()
                .orElse(0);

        try (var input = Files.newInputStream(chemin)) {
            return input.readNBytes(maxLen);
        }
    }

    private  boolean commenceParSignature(byte[] data, byte[] signature) {
        if (data.length < signature.length) {
            return false;
        }
         return Arrays.equals(data, 0, signature.length, signature, 0, signature.length);
    }

    private Charset resoudreCharset(String nomCharset) {
        try {
            return Charset.forName(nomCharset);
        } catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
            throw new ConversionException(
                    "Encodage non supporté : " + nomCharset + ". Utilisez UTF-8 ou ISO-8859-1.", e);
        }
    }
}
