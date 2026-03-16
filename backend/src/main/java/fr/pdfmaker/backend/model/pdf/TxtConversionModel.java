package fr.pdfmaker.backend.model.pdf;

import lombok.Getter;

@Getter
public class TxtConversionModel {

    private final String pageFormat = "A4";
    private final String orientation = "portrait";
    private final String font = "COURIER";
    private final int fontSize = 12;
    private final float marginTop = 20.0f;
    private final float marginBottom = 20.0f;
    private final float marginLeft = 20.0f;
    private final float marginRight = 20.0f;
    private final float lineSpacing = 1.5f;
    private final boolean showPageNumbers = true;
    private final String encoding = "UTF-8";
}
