package fr.pdfmaker.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import fr.pdfmaker.backend.exception.FichierIntrouvableException;
import fr.pdfmaker.backend.exception.UnsupportedFormatException;
import fr.pdfmaker.backend.model.dto.conversion.ConversionResultatDto;
import fr.pdfmaker.backend.model.dto.conversion.TxtConversionRequestDto;
import fr.pdfmaker.backend.service.conversion.TxtToPdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
public class TxtToPdfServiceTest {

    @InjectMocks
    private TxtToPdfService txtToPdfService; // Adapte le nom exact de ta classe

    private TxtConversionRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = new TxtConversionRequestDto();
        requestDto.setCharset("UTF-8");
        requestDto.setNomFichierSortie("mon_doc_converti");
    }

    @Test
    void convert_TexteValide_Succes() {
        // Arrange : On crée un faux fichier TXT propre
        String contenuTexte = "Bonjour,\nceci est un test de conversion.";
        MockMultipartFile fichierTexte = new MockMultipartFile(
                "fichier",
                "test.txt",
                "text/plain",
                contenuTexte.getBytes(StandardCharsets.UTF_8)
        );
        requestDto.setFichier(fichierTexte);

        // Act
        ConversionResultatDto resultat = txtToPdfService.convert(requestDto);

        // Assert
        assertNotNull(resultat);
        assertEquals("mon_doc_converti.pdf", resultat.getNomFichierSortie());
        assertNotNull(resultat.getFichierPdf()); // Le PDF généré en octets
    }

    @Test
    void convert_FichierBinaireDetecte_DevraitLeverException() {
        // Arrange : On crée un fichier qui commence par la signature d'un PDF (%PDF)
        byte[] fauxContenuTxt = new byte[]{ 0x25, 0x50, 0x44, 0x46, 0x00, 0x00 };
        MockMultipartFile fichierFrauduleux = new MockMultipartFile(
                "fichier",
                "faux_texte.txt",
                "text/plain",
                fauxContenuTxt
        );
        requestDto.setFichier(fichierFrauduleux);

        // Act & Assert
        assertThrows(UnsupportedFormatException.class, () -> {
            txtToPdfService.convert(requestDto);
        });
    }

    @Test
    void convert_FichierVide_DevraitLeverException() {
        // Arrange
        MockMultipartFile fichierVide = new MockMultipartFile("fichier", "", "text/plain", new byte[0]);
        requestDto.setFichier(fichierVide);

        // Act & Assert
        assertThrows(FichierIntrouvableException.class, () -> {
            txtToPdfService.convert(requestDto);
        });
    }
}