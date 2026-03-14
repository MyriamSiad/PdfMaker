package fr.pdfmaker.backend.enums;

import lombok.Getter;

@Getter
public enum SupportedFormatDto {

        JPG("jpg"),
        JPEG("jpeg"),
        PNG("png"),
        TXT("txt"),
        MD("md");

    private final String extension;
    SupportedFormatDto(String extension){
        this.extension = extension;
    }
}
