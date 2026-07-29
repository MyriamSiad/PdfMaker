package fr.pdfmaker.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import fr.pdfmaker.backend.exception.UnsupportedFormatException;
import fr.pdfmaker.backend.service.conversion.ImageToPdfService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
public class ImageToPdfServiceTest {

    @Mock
    private FichierService fichierService; // Mock de ton utilitaire si nécessaire

    @InjectMocks
    private ImageToPdfService imageToPdfService;

    @Test
    void verifierFormat_JpegValide_NeLevePasDException() {
        // Un tableau d'octets qui commence par le Magic Byte JPEG (0xFF, 0xD8, 0xFF)
        byte[] magicBytesJpeg = new byte[]{ (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00 };

        // Act & Assert : Si aucune exception n'est levée, le test passe
        assertDoesNotThrow(() -> imageToPdfService.verifierFormat(magicBytesJpeg));
    }

    @Test
    void verifierFormat_FormatInvalide_DevraitLeverException() {
        // Signature d'un fichier GIF (0x47, 0x49, 0x46, 0x38) qui n'est pas géré par ton service
        byte[] magicBytesGif = new byte[]{ 0x47, 0x49, 0x46, 0x38, 0x00, 0x00, 0x00, 0x00 };

        // Act & Assert
        assertThrows(UnsupportedFormatException.class, () -> {
            imageToPdfService.verifierFormat(magicBytesGif);
        });
    }

    @Test
    void lireMagicBytes_TableauTropCourt_RetourneNull() throws Exception {
        // Ton code attrape IndexOutOfBoundsException et renvoie null si le tableau fait moins de 8 octets
        byte[] court = new byte[]{ 0x01, 0x02 };

        byte[] resultat = imageToPdfService.lireMagicBytes(court);

        assertNull(resultat);
    }
}