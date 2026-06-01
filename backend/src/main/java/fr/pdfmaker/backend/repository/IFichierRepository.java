package fr.pdfmaker.backend.repository;

import fr.pdfmaker.backend.model.entity.Fichier;
import fr.pdfmaker.backend.model.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface IFichierRepository  extends JpaRepository<Fichier,Long> {


    Fichier findByUtilisateur(Utilisateur utilisateur);

    Fichier findByidFichier(Long idFichier);

    Fichier findByNomOriginal(String nomDuFichierOriginal);

    Fichier findByNomStockage(String nomStockage);

    Fichier findByCheminLocal(String nomCheminLocal);

    List<Fichier> findFichiersByDossier_IdDossier(Long idDossier);

}
