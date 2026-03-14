package fr.pdfmaker.backend.service;

import fr.pdfmaker.backend.exception.FileNotFoundException;
import fr.pdfmaker.backend.exception.UnsupportedFormatException;
import fr.pdfmaker.backend.model.dto.ConversionResultatDto;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BaseConversionService implements IConversionService {


    @Override
    public final ConversionResultatDto convertToPdf(String inputPath, String outputPath) {
        try {
            validate(inputPath, outputPath);
            return doConvert(inputPath, outputPath);

        } catch (FileNotFoundException e) {
            return ConversionResultatDto.builder()
                    .success(false)
                    .outputPath(null)
                    .errorMessage(e.getMessage())
                    .build();

        } catch (UnsupportedFormatException e) {
            return ConversionResultatDto.builder()
                    .success(false)
                    .outputPath(null)
                    .errorMessage(e.getMessage())
                    .build();
        }

    }

    private void validate(String inputPath, String outputPath) {
        if (inputPath == null || inputPath.isEmpty()) {
            throw new FileNotFoundException(inputPath);
        }

        if (!Files.exists(Paths.get(inputPath))) {
            throw new FileNotFoundException(inputPath);
        }

        String extension = inputPath.substring(inputPath.lastIndexOf(".") + 1);
        if (!List.of("jpg", "jpeg", "png", "txt", "md").contains(extension.toLowerCase())) {
            throw new UnsupportedFormatException(extension);
        }
    }

    public ConversionResultatDto doConvert(String inputPath, String outputPath) {
        return null;
    }
}
