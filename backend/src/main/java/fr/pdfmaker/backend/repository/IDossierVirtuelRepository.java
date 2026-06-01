package fr.pdfmaker.backend.repository;

import fr.pdfmaker.backend.model.entity.DossierVirtuel;
import fr.pdfmaker.backend.model.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IDossierVirtuelRepository extends JpaRepository<DossierVirtuel,Long> {

    DossierVirtuel findByUtilisateur(Utilisateur utilisateur);

    DossierVirtuel findByidDossier(Long idDossier);

    Set<DossierVirtuel> findByUtilisateurIdUser(Long idUser);

    DossierVirtuel findByNomDuDossier(String nomDuDossier);

    //List<DossierVirtuel> findByDossierParent(DossierVirtuel dossierParent);

}

