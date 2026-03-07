package fr.pdfmaker.backend.service;

public interface ConversionService {

    /*void convertJpegToPdf(String inputPathFichier, String outputPathFichier);
    void convertPngToPdf(String inputPathFichier , String outputPathFichier);
    void convertTxtToPdf(String inputPathFichier , String outputPathFichier);
    */
        void convertToPdf(String inputPathFichier, String outputPathFichier);

        //Toutes mes classes vont implémenter cette méthode convertToPdf.

    //Important verfier dans le service si le format d'entrée est supporté ou pas, et si oui, faire la conversion, sinon, retourner une erreur. Et aussi, pour le chemin de sortie, vérifier que le format de sortie est bien un pdf, sinon, retourner une erreur.
}
