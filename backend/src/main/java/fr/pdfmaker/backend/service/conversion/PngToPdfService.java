package fr.pdfmaker.backend.service.conversion;

import fr.pdfmaker.backend.model.dto.conversion.ConversionRequestDto;
import fr.pdfmaker.backend.model.dto.conversion.ConversionResultatDto;

public class PngToPdfService  implements IConversionService {

    @Override
    public ConversionResultatDto convertToPdf(String inputPathFichier) {
        return null;
    }

    @Override
    public ConversionResultatDto convert(ConversionRequestDto request) {
        return null;
    }

    @Override
    public void verifierFormat(byte[] magicBytes) {

    }
}
