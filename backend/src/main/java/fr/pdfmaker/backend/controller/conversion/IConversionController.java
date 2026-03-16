package fr.pdfmaker.backend.controller.conversion;

import org.springframework.http.ResponseEntity;

/**
 * Interface pour le contrôleur de conversion. Cette interface définit les méthodes que le contrôleur de conversion doit implémenter pour gérer les requêtes liées à la conversion de fichiers en PDF.
 * Les méthodes peuvent inclure des opérations telles que la conversion de fichiers, la récupération de liste des conversions effectuées, la suppression de conversions, etc.
 * L'implémentation de cette interface sera réalisée dans une classe concrète, par exemple ConversionController, qui contiendra la logique métier pour traiter les requêtes de conversion.
 *
 */
public interface IConversionController {

    /**
     * Fonction pour récupérer un fichier à partir de son chemin complet. Cette méthode peut être utilisée pour télécharger un fichier converti ou pour accéder à un fichier stocké sur le serveur.
     * @param fullPath
     * @return le fichier sous forme de tableau de bytes, encapsulé dans une ResponseEntity pour permettre une gestion appropriée des en-têtes HTTP et du statut de la réponse.
     */
    ResponseEntity<byte[]> getFichier(String fullPath);





}
