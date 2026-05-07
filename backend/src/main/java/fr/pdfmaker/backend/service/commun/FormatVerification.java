package fr.pdfmaker.backend.service.commun;

public class VerifyFormatPdf {

    public boolean verifyFormatPdf(byte [] fichierPdf) {
        byte[] pdfMagicBytes = { 0x25, 0x50, 0x44, 0x46 };

        for  (int i = 0; i < pdfMagicBytes.length; i++) {
            if (pdfMagicBytes[i] != fichierPdf[i]) {
                return false;
            }
        }
        return true;
    }
}
