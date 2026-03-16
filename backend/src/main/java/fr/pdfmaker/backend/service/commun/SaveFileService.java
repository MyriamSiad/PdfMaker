package fr.pdfmaker.backend.service.commun;

//import com.itextpdf.io.exceptions.IOException;

import fr.pdfmaker.backend.exception.ConversionException;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SaveFileService {
    public void enregistrerSous(byte[] fichier , Path path) {


        try {
            Files.createDirectories(path.getParent());
            Files.write(path, fichier);
        } catch (IOException e) {
            throw new ConversionException("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }
}
