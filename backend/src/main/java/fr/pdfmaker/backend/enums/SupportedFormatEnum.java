package fr.pdfmaker.backend.enums;

import lombok.Getter;

@Getter
public enum SupportedFormatEnum {

        JPG("jpg"),
        JPEG("jpeg"),
        PNG("png"),
        TXT("txt");

    private final String extension;
    SupportedFormatEnum(String extension){
        this.extension = extension;
    }
}
